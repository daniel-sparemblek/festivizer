# -*- coding: utf-8 -*-
import flask
import sys
from flask import request, jsonify
from sqlalchemy import create_engine, or_
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
        new_user_isPending = request.values.get('isPending')

        same_username = session.query(User).filter_by(username = new_user_username).all()
        same_email = session.query(User).filter_by(email = new_user_email).all()
        same_phone = session.query(User).filter_by(phone = new_user_phone).all()

        if len(same_username) != 0:
            return "username_exists"
        elif len(same_email) != 0:
            return "email_exists"
        elif len(same_phone) != 0:
            return "phone_exists"

        if new_user_role == "leader":
            new_user_isPending = 1
        else: new_user_isPending = 0

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
                        role = new_user_role, isPending = new_user_isPending)
        session.add(new_user)

        if new_user_role == "organizer":
            all_festivals = session.query(Festival).all()
            new_organizer = session.query(User).filter_by(username = new_user_username).all()

            for festival in all_festivals:
                new_org_fest = FestivalOrganizers(festival_id = festival.festival_id, organizer_id = new_organizer[0].user_id, status = -2 )
                session.add(new_org_fest)
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

@app.route('/admin/send_decision', methods=['POST'])
def handle_request_four():
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        admin_identifier = request.values.get('user_identifier')
        admin_password = request.values.get('password')
        leader_username = request.values.get('leader_username')
        decision = request.values.get('decision')

        requested_admin = session.query(User).filter_by(username = admin_identifier).all()

        if len(requested_admin) == 0:
            requested_admin = session.query(User).filter_by(email = admin_identifier).all()
            if len(requested_admin) == 0:
                return "no_user"

        if(requested_admin[0].password == admin_password):
            session.commit()
            if(requested_admin[0].role == "administrator"):
                res = session.query(User).filter_by(isPending = True, role = "leader", username = leader_username).all()

                if(decision == 'accept'):
                    res[0].isPending = False
                    session.commit()
                    return "success"

                if(decision == 'decline'):
                    res[0].isPending = False
                    session.commit()
                    return "success"
            else:
                return "permission_denied"
        return "wrong_password"

@app.route('/leader/<username>/get_organizers', methods=['POST'])
def handle_request_five(username):
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
                query_res = session.query(org.username, Festival.festival_id, Festival.name).join(FestivalOrganizers, FestivalOrganizers.organizer_id == org.user_id).join(Festival, Festival.festival_id == FestivalOrganizers.festival_id).filter(org.role == "organizer", Festival.creator_id == requested_leader[0].user_id).filter(FestivalOrganizers.status == -1).all()

                print >> sys.stderr, "PROBA"
                str_res = ""
                for row in query_res:
                    str_res = str_res + row[0] + " " + str(row[1]) + " " + row[2] + ";"

                return str_res
            else:
                return "permission_denied"
        return "wrong_password"

@app.route('/leader/<username>/send_decision', methods=['POST'])
def handle_request_six(username):
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

            if(requested_leader[0].role == "leader"):
                query_res = session.query(FestivalOrganizers).join(User, User.user_id == FestivalOrganizers.organizer_id).filter(User.username == organizer_username, User.isPending == True, FestivalOrganizers.festival_id == int(festival)).all()
                query_user = session.query(User).filter(User.username == organizer_username).all()

                print >> sys.stderr, leader_identifier
                print >> sys.stderr, leader_password
                print >> sys.stderr, organizer_username
                print >> sys.stderr, festival
                print >> sys.stderr, decision
                print >> sys.stderr, "test"

                if decision == 'accept': #1
                    org_fest = session.query(FestivalOrganizers).filter_by(organizer_id = query_user[0].user_id, festival_id = int(festival)).all()
                    org_fest[0].status = 1
                    session.commit()
                    return "success"

                if decision == 'decline': #0
                    org_fest = session.query(FestivalOrganizers).filter_by(organizer_id = query_user[0].user_id, festival_id = int(festival)).all()
                    org_fest[0].status = 0
                    session.commit()
                    return "success"

            else:
                return "permission_denied"
        return "wrong_password"

@app.route('/festival/<org_username>', methods=['GET'])
def handle_request_seven(org_username):
    if request.method == 'GET':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        requested_organizer = session.query(User).filter_by(username = org_username).all()
        requested_festivals = session.query(Festival, FestivalOrganizers.status).join(FestivalOrganizers, FestivalOrganizers.festival_id == Festival.festival_id).filter(or_(FestivalOrganizers.status == -2, FestivalOrganizers.status == -1)).filter_by(organizer_id = requested_organizer[0].user_id)

        all_festivals = ""
        for festival in requested_festivals:
            all_festivals = all_festivals + festival[0].name + "," + str(festival[1]) + ";"

        return all_festivals

@app.route('/festival/apply', methods=['POST'])
def handle_request_eight():
    if request.method == 'POST':
        DBSession = sessionmaker(bind=engine)
        session = DBSession()

        festival_name = request.values.get('festivalName')
        organizer_username = request.values.get('username')
        decision = request.values.get('status')

        print >> sys.stderr, organizer_username
        print >> sys.stderr, festival_name
        print >> sys.stderr, decision


        requested_organizer = session.query(User).filter_by(username = organizer_username).all()
        festival = session.query(Festival).filter_by(name = festival_name).all()

        requested_fest_org = session.query(FestivalOrganizers).filter_by(festival_id = festival[0].festival_id, organizer_id = requested_organizer[0].user_id).all()

        if decision == 'Apply':
            requested_fest_org[0].status = -1

        if decision == 'Cancel':
            requested_fest_org[0].status = -2

        #print >> sys.stderr, festival_name
        #print >> sys.stderr, organizer_username

        #festival = session.query(Festival).filter(Festival.name == festival_name).all()
        #organizer = session.query(User).filter(User.username == organizer_username).all()

        #new_row = FestivalOrganizers(festival_id = festival[0].festival_id, organizer_id = organizer[0].user_id)
        #session.add(new_row)

        session.commit()
        return "success"

