from flask import Flask
from flask_jwt_extended import JWTManager, jwt_refresh_token_required, get_jwt_identity
from flask_marshmallow import Marshmallow
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.engine import Engine
from sqlalchemy import event
import sqlite3

app = Flask(__name__)
api = Api(app)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'b\'\xbe\xadI\x13\xaf\x959f\x18=\x8d\x06Q\x9a\xd6y\x93\xee52\x01\x15B\x83\''
app.config['JWT_SECRET_KEY'] = 'b\'\x856\xbb\xdf\xf49S\xfep\x1eJEc>\xf1\xec(\x08n#6\x01*\xa6\''
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']
app.config['PROPAGATE_EXCEPTIONS'] = True

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
    @event.listens_for(Engine, "connect")
    def set_sqlite_pragma(dbapi_connection, connection_record):
        if type(dbapi_connection) is sqlite3.Connection:  # play well with other DB backends
            cursor = dbapi_connection.cursor()
            cursor.execute("PRAGMA foreign_keys=ON")
            cursor.close()

    db.create_all()
    db.query_expression


api.add_resource(resources.Users, '/users')
api.add_resource(resources.Workers, '/workers')
api.add_resource(resources.Leaders, '/leaders')
api.add_resource(resources.Organizers, '/organizers')
api.add_resource(resources.Specializations, '/specializations')
api.add_resource(resources.SpecializationAdd, '/specializations/<string:specialization_id>/add')
api.add_resource(resources.Applications, '/applications')
api.add_resource(resources.UserRegistration, '/registration')
api.add_resource(resources.UserLogin, '/login')
api.add_resource(resources.User, '/user/<string:username>')
api.add_resource(resources.AvailableJobs, '/jobs/on_auction')

api.add_resource(resources.Festival, '/festival')
api.add_resource(resources.FestivalUnique, '/festival/<string:festival_id>')
api.add_resource(resources.FestivalApply, '/festival/<string:festival_id>/apply')
api.add_resource(resources.FestivalOrganizersPending, '/festival/<string:festival_id>/organizers/pending')
api.add_resource(resources.FestivalOrganizersAccepted, '/festival/<string:festival_id>/organizers/accepted')
api.add_resource(resources.Festivals, '/festivals')
api.add_resource(resources.SearchUsers, '/search/users')
api.add_resource(resources.SearchSpecializations, '/search/specializations')
api.add_resource(resources.Events, '/events')
api.add_resource(resources.Event, '/event')
api.add_resource(resources.EventUnique, '/event/<string:event_id>')
api.add_resource(resources.Job, '/job')
api.add_resource(resources.Jobs, '/jobs')
api.add_resource(resources.Auction, '/auction')
api.add_resource(resources.Auctions, '/auctions')

api.add_resource(resources.UserLogoutAccess, '/logout/access')
api.add_resource(resources.UserLogoutRefresh, '/logout/refresh')
api.add_resource(resources.TokenRefresh, '/token/refresh')
api.add_resource(resources.SecretResource, '/secret')
api.add_resource(resources.Test, '/test')