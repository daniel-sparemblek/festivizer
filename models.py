import sys

from run import db
from run import ma
from passlib.hash import pbkdf2_sha256 as sha256

# MODELS


class UserModel(db.Model):
    __tablename__ = 'users'

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(120), unique=True, nullable=False)
    password = db.Column(db.String(120), nullable=False)
    first_name = db.Column(db.String(50), nullable=False)
    last_name = db.Column(db.String(50), nullable=False)
    picture = db.Column(db.String(250), nullable=False)
    phone = db.Column(db.String(50), unique=True)
    email = db.Column(db.String(50), unique=True)
    role = db.Column(db.String(50), nullable=False)

    @classmethod
    def find_by_username(cls, username):
        return cls.query.filter_by(username=username).first()

    @classmethod
    def return_all(cls):
        users_schema = UserSchema(many=True)
        users = db.session.query(cls).all()
        db.session.commit()
        result = users_schema.dump(users)
        response = {
            'status': 'success',
            'data': result,
            'message': '{} users sent'.format(len(users))
        }, 200
        return response

    @classmethod
    def delete_all(cls):
        try:
            num_rows_deleted = db.session.query(cls).delete()
            db.session.commit()
            return {
                'status': 'success',
                'data': None,
                'message': '{} row(s) deleted'.format(num_rows_deleted),
            }, 200
        except Exception as e:
            print(str(e), sys.err)
            return {
                'success': 'error',
                'data': None,
                'message': 'Internal server error'
            }, 500

    @staticmethod
    def generate_hash(password):
        return sha256.hash(password)

    @staticmethod
    def verify_hash(password, hashing):
        return sha256.verify(password, hashing)

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class LeaderModel(UserModel):
    __tablename__ = 'leaders'

    id = db.Column(db.Integer, db.ForeignKey('users.id'), primary_key=True)
    is_pending = db.Column(db.Integer)

    @classmethod
    def return_all(cls):
        leaders_schema = LeaderSchema(many=True)
        leaders = db.session.query(cls).all()
        db.session.commit()
        result = leaders_schema.dump(leaders)
        response = {
            'status': 'success',
            'data': result,
            'message': '{} users sent.'.format(len(leaders))
        }, 200
        return response

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class RevokedTokenModel(db.Model):
    __tablename__ = 'revoked_tokens'
    id = db.Column(db.Integer, primary_key=True)
    jti = db.Column(db.String(120))

    def add(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def is_jti_blacklisted(cls, jti):
        query = cls.query.filter_by(jti=jti).first()
        return bool(query)

# SCHEMAS


class UserSchema(ma.ModelSchema):
    class Meta:
        model = UserModel


class LeaderSchema(ma.ModelSchema):
    class Meta:
        model = LeaderModel
