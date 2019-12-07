from flask_restful import Resource
from flask import request, redirect
from decorators import permission_required
from models import UserModel, RevokedTokenModel, UserSchema
from flask_jwt_extended import (create_access_token, create_refresh_token,
                                jwt_required, jwt_refresh_token_required, get_jwt_identity, get_raw_jwt)


class UserRegistration(Resource):
    @staticmethod
    def post():
        data = request.get_json()
        new_user_schema = UserSchema()
        validate = new_user_schema.validate(data)

        if bool(validate):
            value = list(validate.values())[0]
            return {"msg": value[0]}, 422

        if data['permission'] == '1':
            data['is_pending'] = 1
        else:
            data['is_pending'] = None

        if UserModel.find_by_username(data['username']):
            return {
                'msg': 'User {} already exists'.format(data['username'])
            }, 422

        if UserModel.find_by_phone(data['phone']):
            return {
                'msg': 'Phone {} already exists'.format(data['phone'])
            }, 422

        if UserModel.find_by_email(data['email']):
            return {
                'msg': 'User {} already exists'.format(data['email'])
            }, 422

        if data['permission'] == '1':
            new_user = UserModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['first_name'],
                last_name=data['last_name'],
                picture=data['picture'],
                phone=data['phone'],
                email=data['email'],
                permission=data['permission'],
                is_pending=1
            )
        else:
            new_user = UserModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['first_name'],
                last_name=data['last_name'],
                picture=data['picture'],
                phone=data['phone'],
                email=data['email'],
                permission=data['permission'],
                is_pending=None
            )

        try:
            new_user.save_to_db()
            return {
                'msg': 'User {} was created'.format(data['username'])
            }
        except Exception as e:
            return {'msg': 'Internal Server Error,'}, 500


class UserLogin(Resource):
    @staticmethod
    def post():
        data = request.get_json()

        if data.get('username') is None:
            return {"msg": "Missing username."}, 422

        if data.get('password') is None:
            return {"msg": "Missing password."}, 422

        current_user = UserModel.find_by_username(data['username'])

        if not current_user:
            return {'msg': 'User {} doesn\'t exist'.format(data['username'])}, 404

        if UserModel.verify_hash(data['password'], current_user.password):
            access_token = create_access_token(identity=data['username'])
            refresh_token = create_refresh_token(identity=data['username'])
            return {
                'msg': 'Logged in as {}'.format(current_user.username),
                'access_token': access_token,
                'refresh_token': refresh_token
            }, 200
        else:
            return {'msg': 'Wrong credentials'}, 400


class User(Resource):
    @jwt_required
    def get(self, username):
        user = UserModel.find_by_username(username)
        if user:
            return UserSchema().dump(user)
        else:
            return {"msg": "The requested URL /{} was not found on this server.".format(username)}, 404


class Users(Resource):
    @jwt_required
    @permission_required(0)
    def get(self):
        permission = request.args.get('permission')
        is_pending = request.args.get('is_pending')

        if len(list(request.args)) == 0:
            return UserSchema(many=True).dump(UserModel.return_all()), 200

        if permission and is_pending and len(list(request.args)) == 2:
            users = UserModel.query.filter_by(permission=permission, is_pending=is_pending).all()
            return UserSchema(many=True).dump(users), 200
        else:
            return redirect("http://localhost:5000/users")



    @jwt_required
    @permission_required(0)
    def delete(self):
        return UserModel.delete_all()


class UsersFilterByPermissionAndFilterByIsPending(Resource):
    @jwt_required
    def get(self, permission, is_pending):
        users = UserModel.query.filter_by(permission=permission, is_pending=is_pending).all()
        return UserSchema(many=True).dump(users)


class UserLogoutAccess(Resource):
    @jwt_required
    def post(self):
        jti = get_raw_jwt()['jti']
        try:
            revoked_token = RevokedTokenModel(jti=jti)
            revoked_token.add()
            return {'msg': 'Access token has been revoked.'}
        except:
            return {'msg': 'Internal server error.'}, 500


class UserLogoutRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        jti = get_raw_jwt()['jti']
        try:
            revoked_token = RevokedTokenModel(jti = jti)
            revoked_token.add()
            return {'message': 'Refresh token has been revoked'}
        except:
            return {'message': 'Something went wrong'}, 500


class TokenRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        current_user = get_jwt_identity()
        access_token = create_access_token(identity = current_user)
        return {'access_token': access_token}


class Test(Resource):
    def post(self):
        return {'message': 'Test passed!'}


class SecretResource(Resource):
    @jwt_required
    def get(self):
        return {
            'answer': 42
        }
