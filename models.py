from marshmallow import validates, ValidationError, fields, validates_schema
from run import db
from run import ma
import re
from passlib.hash import pbkdf2_sha256 as sha256


# MODELS


class UserModel(db.Model):
    __tablename__ = 'users'

    def __init__(self, username, password, first_name, last_name, picture, phone, email, permission, is_pending):
        self.username = username
        self.password = password
        self.first_name = first_name
        self.last_name = last_name
        self.picture = picture
        self.phone = phone
        self.email = email
        self.permission = permission
        self.is_pending = is_pending

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
    def find_by_username_start(cls, start):
        search = "{}%".format(start)
        return cls.query.filter(cls.username.like(search)).all()

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


class FestivalModel(db.Model):
    __tablename__ = 'festivals'

    festival_id = db.Column(db.Integer, primary_key=True)
    leader_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    desc = db.Column(db.String(250), nullable=True)
    logo = db.Column(db.String(250), nullable=True)
    start_time = db.Column(db.DateTime, nullable=False)
    end_time = db.Column(db.DateTime, nullable=False)
    status = db.Column(db.Integer, nullable=False)

    @classmethod
    def find_by_leader_id(cls, leader_id):
        return cls.query.filter_by(leader_id=leader_id).all()

    @classmethod
    def return_all(cls):
        return cls.query.all()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class FestivalOrganizers(db.Model):
    __tablename__ = 'festival_organizers'

    festival_organizers_id = db.Column(db.Integer, primary_key=True)
    festival_id = db.Column(db.Integer, db.ForeignKey('festivals.festival_id'))
    organizer_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    status = db.Column(db.Integer, nullable=False)
    __table_args__ = (db.UniqueConstraint('festival_id', 'organizer_id'), )


class EventModel(db.Model):
    __tablename__ = 'events'

    event_id = db.Column(db.Integer, primary_key=True)
    festival_id = db.Column(db.Integer, nullable=False)
    organizer_id = db.Column(db.Integer, nullable=False)
    name = db.Column(db.String(50), nullable=False)
    desc = db.Column(db.String(250), nullable=True)
    location = db.Column(db.String, nullable=False)
    start_time = db.Column(db.DateTime, nullable=False)
    end_time = db.Column(db.DateTime, nullable=False)
    __table_args__ = (db.UniqueConstraint('festival_id', 'organizer_id', 'name', 'location', 'start_time'), )
    db.ForeignKeyConstraint(['festival_id', 'organizer_id'],
                            ['festival_organizers.festival_id', 'festival_organizers.organizer_id'])

    @classmethod
    def find_by_festival_id(cls, festival_id):
        return cls.query.filter_by(festival_id=festival_id)


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


class FestivalSchema(ma.ModelSchema):
    festival_id = fields.Integer(required=False)
    leader_id = fields.Integer(required=True, error_messages={"required": "Missing leader."})
    name = fields.Str(required=True, error_messages={"required": "Missing festival's name."})
    desc = fields.Str(required=False)
    logo = fields.Str(required=False)
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})
    status = fields.Integer(required=True, error_messages={"required": "Missing status."})

    @validates('status')
    def validate_status(self, value):
        if value != 0 and value != 1 and value != 2:
            raise ValidationError("Status must be 0, 1 or 2.")

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            festival_schema = FestivalSchema(many=True)
            return festival_schema.dump(value)
        return FestivalSchema().dump(value)

    class Meta:
        model = FestivalModel


class EventSchema(ma.ModelSchema):
    event_id = fields.Integer(required=False)
    festival_id = fields.Integer(required=True, error_messages={"required": "Missing festival's id."})
    organizer_id = fields.Integer(required=True, error_messages={"required": "Missing organizer's id."})
    name = fields.Str(required=True, error_messages={"required": "Missing name."})
    desc = fields.Str(required=True, error_messages={"required": "Missing description."})
    location = fields.Str(required=True, error_messages={"required": "Missing location."})
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            event_schema = EventSchema(many=True)
            return event_schema.dump(value)
        return EventSchema().dump(value)

    class Meta:
        model = EventModel
