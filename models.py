from marshmallow import validates, ValidationError, fields
from run import db
from run import ma
import re
from passlib.hash import pbkdf2_sha256 as sha256

# MODELS


class UserModel(db.Model):
    __tablename__ = 'users'

    def __init__(self, username, password, first_name, last_name, picture, phone, email, permission, is_pending):
        self.username=username
        self.password=password
        self.first_name=first_name
        self.last_name=last_name
        self.picture=picture
        self.phone=phone
        self.email=email
        self.permission=permission
        self.is_pending=is_pending

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(120), unique=True, nullable=False)
    password = db.Column(db.String(120), nullable=False)
    first_name = db.Column(db.String(50), nullable=False)
    last_name = db.Column(db.String(50), nullable=False)
    picture = db.Column(db.String(250), nullable=False)
    phone = db.Column(db.String(50), unique=True)
    email = db.Column(db.String(50), unique=True)
    permission = db.Column(db.Integer, nullable=False)
    is_pending = db.Column(db.Integer, nullable=True)

    @classmethod
    def find_by_id(cls, user_id):
        return cls.query.filter_by(id=user_id).first()

    @classmethod
    def find_by_username(cls, username):
        return cls.query.filter_by(username=username).first()

    @classmethod
    def find_by_phone(cls, phone):
        return cls.query.filter_by(phone=phone).first()

    @classmethod
    def find_by_email(cls, username):
        return cls.query.filter_by(username=username).first()

    @classmethod
    def return_all(cls):
        return cls.query.all()

    @classmethod
    def delete_all(cls):
        db.session.query(cls).delete()
        db.session.commit()

    @staticmethod
    def generate_hash(password):
        return sha256.hash(password)

    @staticmethod
    def verify_hash(password, hashing):
        return sha256.verify(password, hashing)

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

    id = fields.Integer(required=False)
    username = fields.Str(required=True, error_messages={"required": "Missing username."})
    password = fields.Str(required=True, error_messages={"required": "Missing password"})
    first_name = fields.Str(required=True, error_messages={"required": "Missing first name."})
    last_name = fields.Str(required=True, error_messages={"required": "Missing last name."})
    picture = fields.Str(required=True, error_messages={"required": "Missing picture."})
    phone = fields.Str(required=True, error_messages={"required": "Missing phone."})
    email = fields.Str(required=True, error_messages={"required": "Missing email."})
    permission = fields.Int(required=True, error_messages={"required": "Missing permission."})

    @validates('first_name')
    def validate_first_name(self, value):
        if not value[0].isupper():
            raise ValidationError("Name should start with capital letter.")

    @validates('last_name')
    def validate_last_name(self, value):
        if not value[0].isupper():
            raise ValidationError("Last name should start with capital letter.")

    @validates('username')
    def validate_username(self, value):
        if not value[0].isalpha():
            raise ValidationError("Username must start with letter.")
        if not re.match("^[A-Za-z0-9_]+$", value):
            raise ValidationError("Username can't have special signs.")

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            user_schema = UserSchema(many=True)
            return user_schema.dump(value)
        return UserSchema().dump(value)

    class Meta:
        model = UserModel
