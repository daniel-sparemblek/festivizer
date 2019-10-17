# -*- coding: utf-8 -*-
import flask
import sqlite3
import io
from PIL import Image
from flask import request
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy import exc
from sqlalchemy.orm import exc as exc2
from database_setup import User, Base

engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
session = DBSession()
    
app = flask.Flask(__name__)

@app.route('/register', methods=['POST'])
def handle_request_one():
    if request.method == 'POST':
        user_name = request.values.get('username')
        userPassword = request.values.get('password')
        first_name = request.values.get('firstname')
        last_name = request.values.get('lastname')
        userPicture = request.values.get('picture')
        userPhone = request.values.get('phone')
        userEmail = request.values.get('email')
        userPermission = request.values.get('permission')

        s1 = userPicture
        s2 = s1[1:-1]
        ba = s2.split(", ")
        bafin = []
        for x in ba:
            bafin.append(int(x))
        f = open('./images/' + user_name + '.jpeg', 'w+b')
        f.write(bytearray(bafin))
        f.close()
        picture_path = './images/'+user_name+'.jpeg'

        userOne = User(username = user_name, password = userPassword, firstname = first_name, lastname = last_name, picture = picture_path, phone = userPhone, email = userEmail, permission = userPermission)
        try:
            session.add(userOne)
            session.commit()
        except:
            session.rollback()
            return "username_exists"
        return "success"    

app.run(host="0.0.0.0", port=5000, debug=True)
