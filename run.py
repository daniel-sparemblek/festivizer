from flask import Flask
from flask_jwt_extended import JWTManager
from flask_marshmallow import Marshmallow
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
api = Api(app)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'b\'\xbe\xadI\x13\xaf\x959f\x18=\x8d\x06Q\x9a\xd6y\x93\xee52\x01\x15B\x83\''
app.config['JWT_SECRET_KEY'] = 'b\'\x856\xbb\xdf\xf49S\xfep\x1eJEc>\xf1\xec(\x08n#6\x01*\xa6\''
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']

jwt = JWTManager(app)
db = SQLAlchemy(app)
ma = Marshmallow(app)

import models, resources


@jwt.token_in_blacklist_loader
def check_if_token_in_blacklist(decrypted_token):
    jti = decrypted_token['jti']
    return models.RevokedTokenModel.is_jti_blacklisted(jti)


@app.before_first_request
def create_tables():
    db.create_all()


api.add_resource(resources.UserRegistration, '/registration')
api.add_resource(resources.UserLogin, '/login')
api.add_resource(resources.UserLogoutAccess, '/logout/access')
api.add_resource(resources.UserLogoutRefresh, '/logout/refresh')
api.add_resource(resources.TokenRefresh, '/token/refresh')
api.add_resource(resources.AllUsers, '/users')
api.add_resource(resources.AllLeaders, '/leaders')
api.add_resource(resources.SecretResource, '/secret')
api.add_resource(resources.Test, '/test')
