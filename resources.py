from flask_restful import Resource, reqparse
from models import UserModel, LeaderModel, RevokedTokenModel
from flask_jwt_extended import (create_access_token, create_refresh_token,
                                jwt_required, jwt_refresh_token_required, get_jwt_identity, get_raw_jwt)
reg_parser = reqparse.RequestParser()
reg_parser.add_argument('username', help='This field cannot be blank', required=True)
reg_parser.add_argument('password', help='This field cannot be blank', required=True)
reg_parser.add_argument('firstName', help='This field cannot be blank', required=True)
reg_parser.add_argument('lastName', help='This field cannot be blank', required=True)
reg_parser.add_argument('picture', help='This field cannot be blank', required=True)
reg_parser.add_argument('phone', help='This field cannot be blank', required=True)
reg_parser.add_argument('email', help='This field cannot be blank', required=True)
reg_parser.add_argument('role', help='This field cannot be blank', required=True)

login_parser = reqparse.RequestParser()
login_parser.add_argument('name', help='This field cannot be blank', required=True)
login_parser.add_argument('password', help="This field cannot be blank", required=True)


class UserRegistration(Resource):
    def post(self):
        data = reg_parser.parse_args()

        if UserModel.find_by_username(data['username']):
            return {
                'status': 'error',
                'data': None,
                'message': 'User {} already exists'.format(data['username'])
            }, 422

        if data['role'] == 'leader':
            new_user = LeaderModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['firstName'],
                last_name=data['lastName'],
                picture=data['picture'],
                phone=data['phone'],
                email=data['email'],
                role=data['role'],
                is_pending=1
            )
        else:
            new_user = UserModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['firstName'],
                last_name=data['lastName'],
                picture=data['picture'],
                phone=data['phone'],
                email=data['email'],
                role=data['role']
            )

        try:
            new_user.save_to_db()
            access_token = create_access_token(identity=data['username'])
            refresh_token = create_refresh_token(identity=data['username'])
            return {
                'message': 'User {} was created'.format(data['username']),
                'access_token': access_token,
                'refresh_token': refresh_token
            }
        except:
            return {'message': 'Something went wrong'}, 500


class UserLogin(Resource):
    def post(self):
        data = login_parser.parse_args()
        current_user = UserModel.find_by_username(data['username'])

        if not current_user:
            return {'message': 'User {} doesn\'t exist'.format(data['username'])}

        if UserModel.verify_hash(data['password'], current_user.password):
            access_token = create_access_token(identity=data['username'])
            refresh_token = create_refresh_token(identity=data['username'])
            return {
                'message': 'Logged in as {}'.format(current_user.username),
                'access_token': access_token,
                'refresh_token': refresh_token
            }
        else:
            return {'message': 'Wrong credentials'}


class UserLogoutAccess(Resource):
    @jwt_required
    def post(self):
        jti = get_raw_jwt()['jti']
        try:
            revoked_token = RevokedTokenModel(jti = jti)
            revoked_token.add()
            return {'message': 'Access token has been revoked'}
        except:
            return {'message': 'Something went wrong'}, 500


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


class AllUsers(Resource):
    def get(self):
        return UserModel.return_all()

    def delete(self):
        return UserModel.delete_all()


class AllLeaders(Resource):
    def get(self):
        return LeaderModel.return_all()

    def delete(self):
        return LeaderModel.delete_all()


class Test(Resource):
    def post(self):
        return {'message': 'Test passed!'}


class SecretResource(Resource):
    @jwt_required
    def get(self):
        return {
            'answer': 42
        }
