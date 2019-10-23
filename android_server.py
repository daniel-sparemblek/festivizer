# -*- coding: utf-8 -*-
import flask
from flask import request
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from database_setup import User, Base

engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
session = DBSession()

app = flask.Flask(__name__)

@app.route('/register', methods=['POST'])
def handle_request_one():
    if request.method == 'POST':
        new_user_username = request.values.get('username')
        new_user_password = request.values.get('password')
        new_user_firstname = request.values.get('firstname')
        new_user_lastname = request.values.get('lastname')
        new_user_picture = request.values.get('picture')
        new_user_phone = request.values.get('phone')
        new_user_email = request.values.get('email')
        new_user_permission = request.values.get('permission')

        same_username = session.query(User).filter_by(username = new_user_username).all()
        same_email = session.query(User).filter_by(email = new_user_email).all()
        same_phone = session.query(User).filter_by(phone = new_user_phone).all()

        if len(same_username) != 0:
            return "username_exists"
        elif len(same_email) != 0:
            return "email_exists"
        elif len(same_phone) != 0:
            return "phone_exists"

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
                        permission = new_user_permission)
        session.add(new_user)
        session.commit()
        return "success"

@app.route('/login', methods=['POST'])
def handle_request_two():
    if request.method == 'POST':
        user_name = request.values.get('username')
        user_password = request.values.get('password')

        requested_user = session.query(User).filter_by(username = user_name).all()

        if len(requested_user) == 0:
            return "no_username"
        else:
            if(requested_user[0].password == user_password):
                session.commit()
                return "success"

        return "wrong_password"
