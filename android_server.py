# -*- coding: utf-8 -*-
import flask
import sys
from flask import request, jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, aliased
from database_setup import User, Base, FestivalOrganizers, Festival

engine = create_engine('sqlite:///organizacijafestivala.db')

def _fk_pragma_on_connect(dbapi_con, con_record):
    dbapi_con.execute('pragma foreign_keys=ON')

from sqlalchemy import event
event.listen(engine, 'connect', _fk_pragma_on_connect)

Base.metadata.bind = engine

app = flask.Flask(__name__)

@app.route('/register', methods=['POST'])
def handle_request_one():
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        new_user_username = request.values.get('username')
        new_user_password = request.values.get('password')
        new_user_firstname = request.values.get('firstname')
        new_user_lastname = request.values.get('lastname')
        new_user_picture = request.values.get('picture')
        new_user_phone = request.values.get('phone')
        new_user_email = request.values.get('email')
        new_user_role = request.values.get('role')

        same_username = session.query(User).filter_by(username = new_user_username).all()
        same_email = session.query(User).filter_by(email = new_user_email).all()
        same_phone = session.query(User).filter_by(phone = new_user_phone).all()

        if len(same_username) != 0:
            return "username_exists"
        elif len(same_email) != 0:
            return "email_exists"
        elif len(same_phone) != 0:
            return "phone_exists"

        is_new_user_pending = False

        if new_user_role == "leader":
            is_new_user_pending = True
        elif new_user_role == "organizer":
            is_new_user_pending = True
        else:
            is_new_user_pending = False

        picture_array_unicode = (new_user_picture[1:-1]).split(", ")
        picture_array_int = []
        for num in picture_array_unicode:
            picture_array_int.append(int(num))

        picture_path = '/home/barbil/mysite/images/' + new_user_username + '.jpeg'
        f = open(picture_path, 'w+b')
        f.write(bytearray(picture_array_int))
        f.close()


        new_user = User(username = new_user_username, password = new_user_password,
                        firstname = new_user_firstname, lastname = new_user_lastname,
                        picture = picture_path, phone = new_user_phone, email = new_user_email,
                        role = new_user_role, isPending = is_new_user_pending)
        session.add(new_user)
        session.commit()
        return "success"

@app.route('/login', methods=['POST'])
def handle_request_two():
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        user_identifier = request.values.get('user_identifier')
        user_password = request.values.get('password')
        requested_user = session.query(User).filter_by(username = user_identifier).all()

        if len(requested_user) == 0:
            requested_user = session.query(User).filter_by(email = user_identifier).all()
            if len(requested_user) == 0:
                return "no_username"

        if(requested_user[0].password == user_password):

            session.commit()
            if(requested_user[0].role == "administrator"):
                return "admin"

            if(requested_user[0].role == "organizer"):
                return "organizer"

            if(requested_user[0].role == "leader"):
                return "leader"

            return "success"

        return "wrong_password"

@app.route('/admin', methods=['POST'])
def handle_request_three():
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        admin_identifier = request.values.get('user_identifier')
        admin_password = request.values.get('password')

        requested_admin = session.query(User).filter_by(username = admin_identifier).all()

        if len(requested_admin) == 0:
            requested_admin = session.query(User).filter_by(email = admin_identifier).all()
            if len(requested_admin) == 0:
                return "no_user"

        if(requested_admin[0].password == admin_password):
            session.commit()
            if(requested_admin[0].role == "administrator"):
                res = session.query(User).filter_by(isPending = True, role = "leader").all()

                return jsonify({'pending_leaders': [r.serialize for r in res]})
            else:
                return "permission_denied"
        return "wrong_password"

@app.route('/leader/<username>/get_organizers', methods=['POST'])
def handle_request_four(username):
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        leader_identifier = request.values.get('user_identifier')
        leader_password = request.values.get('password')

        requested_leader = session.query(User).filter_by(username = leader_identifier).all()

        if len(requested_leader) == 0:
            requested_leader = session.query(User).filter_by(email = leader_identifier).all()
            if len(requested_leader) == 0:
                return "no_user"

        if(requested_leader[0].password == leader_password):
            session.commit()
            if(requested_leader[0].role == "leader"):
                org = aliased(User)
                query_res = session.query(org.username, Festival.name).join(FestivalOrganizers, FestivalOrganizers.organizer_id == org.user_id).join(Festival, Festival.festival_id == FestivalOrganizers.festival_id).filter(org.role == "organizer", org.isPending == True, Festival.creator_id == requested_leader[0].user_id).all()

                str_res = ""
                for row in query_res:
                    str_res = str_res + row[0] + " " + row[1]+";"

                return str_res
            else:
                return "permission_denied"
        return "wrong_password"

@app.route('/leader/<username>/send_decision', methods=['POST'])
def handle_request_five(username):
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        leader_identifier = request.values.get('user_identifier')
        leader_password = request.values.get('password')
        organizer_username = request.values.get('organizer_username')
        festival = request.values.get('festival_id')
        decision = request.values.get('decision')

        requested_leader = session.query(User).filter_by(username = leader_identifier).all()

        if len(requested_leader) == 0:
            requested_leader = session.query(User).filter_by(email = leader_identifier).all()
            if len(requested_leader) == 0:
                return "no_user"

        if(requested_leader[0].password == leader_password):
            session.commit()
            if(requested_leader[0].role == "leader"):
                query_res = session.query(FestivalOrganizers).join(User, User.user_id == FestivalOrganizers.organizer_id).filter(User.username == organizer_username, User.isPending == True, FestivalOrganizers.festival_id == festival).all()
                query_user = session.query(User).filter(User.username == organizer_username)

                if decision == 'accept':
                    query_user.isPending = False
                    session.commit()

                if decision == 'decline':
                    query_res.delete()
                    session.commit()

            else:
                return "permission_denied"
        return "wrong_password"
