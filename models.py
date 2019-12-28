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
    def find_by_organizing_festival_id_pending(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(festival_id == festival_id, FestivalOrganizers.status == 1).all()

    @classmethod
    def find_by_organizing_festival_id_accepted(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(FestivalOrganizers.festival_id == festival_id, FestivalOrganizers.status == 0).all()

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
    def find_by_festival_id(cls, festival_id):
        return cls.query.filter_by(festival_id=festival_id).first()

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
    __table_args__ = (db.UniqueConstraint('festival_id', 'organizer_id'),)

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


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
    __table_args__ = (db.UniqueConstraint('festival_id', 'organizer_id', 'name', 'location', 'start_time'),)
    db.ForeignKeyConstraint(['festival_id', 'organizer_id'],
                            ['festival_organizers.festival_id', 'festival_organizers.organizer_id'])

    @classmethod
    def find_by_festival_id(cls, festival_id):
        return cls.query.filter_by(festival_id=festival_id)

    @classmethod
    def find_by_event_id(cls, event_id):
        return cls.query.filter_by(event_id=event_id).first()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class AuctionModel(db.Model):
    __tablename__ = 'auctions'

    auction_id = db.Column(db.Integer, primary_key=True)
    job_id = db.Column(db.Integer, db.ForeignKey('jobs.job_id'), unique=True)
    start_time = db.Column(db.DateTime, nullable=False)
    end_time = db.Column(db.DateTime, nullable=False)

    @classmethod
    def find_by_auction_id(cls, auction_id):
        return cls.query.filter_by(auction_id=auction_id).first()

    @classmethod
    def find_by_leader_id(cls, leader_id):
        return db.session.query(cls).join(JobModel, JobModel.job_id == cls.job_id)\
            .join(EventModel, EventModel.event_id == JobModel.event_id)\
            .join(FestivalModel, FestivalModel.festival_id == EventModel.festival_id)\
            .filter(FestivalModel.leader_id == leader_id).all()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class JobModel(db.Model):
    __tablename__ = 'jobs'

    job_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    event_id = db.Column(db.Integer, db.ForeignKey('events.event_id'))
    worker_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    start_time = db.Column(db.DateTime, nullable=False)
    is_completed = db.Column(db.Boolean, nullable=False)

    @classmethod
    def find_by_job_id(cls, job_id):
        return cls.query.filter_by(job_id=job_id).first()

    @classmethod
    def find_jobs_on_auction_by_leader_id(cls, leader_id):
        return db.session.query(cls).join(AuctionModel, AuctionModel.job_id == cls.job_id) \
            .join(EventModel, EventModel.event_id == JobModel.event_id) \
            .join(FestivalModel, FestivalModel.festival_id == EventModel.festival_id) \
            .filter(FestivalModel.leader_id == leader_id).all()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def get_all(cls):
        return cls.query.all()


class Application(db.Model):
    __tablename__ = 'application'

    application_id = db.Column(db.Integer, primary_key=True)
    auction_id = db.Column(db.Integer, db.ForeignKey('auctions.auction_id'))
    worker_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    price = db.Column(db.Float, nullable=False)
    comment = db.Column(db.String(500), nullable=True)
    duration = db.Column(db.Integer, nullable=False)
    people_number = db.Column(db.Integer, nullable=False)
    __table_args__ = (db.UniqueConstraint('auction_id', 'worker_id'), )

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


class AuctionSchema(ma.ModelSchema):
    auction_id = fields.Integer(required=False)
    job_id = fields.Integer(required=True, error_messages={"required": "Missing job id."})
    start_time = fields.DateTime(required=True, error_messages={"required:": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            event_schema = cls(many=True)
            return event_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = AuctionModel


class JobSchema(ma.ModelSchema):
    job_id = fields.Integer(required=False)
    name = fields.String(required=True, error_messages={"required": "Missing name."})
    event_id = fields.Integer(required=True, error_messages={"required": "Missing event id."})
    worker_id = fields.Integer(required=True, error_messages={"required": "Missing worker id."})
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time"})
    is_completed = fields.Boolean(required=True, error_messages={"required": "Missing completion"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            event_schema = cls(many=True)
            return event_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = JobModel


class ApplicationSchema(ma.ModelSchema):
    application_id = fields.Integer(required=False)
    auction_id = fields.Integer(required=True, error_messages={"required": "Missing auction id"})
    worker_id = fields.Integer(required=True, error_messages={"required": "Missing worker id"})
    price = fields.Float(required=True, error_messages={"required": "Missing price"})
    comment = fields.String(required=False)
    duration = fields.Integer(required=True, error_messages={"required": "Missing duration"})
    people_number = fields.Integer(required=True, error_messages={"required": "Missing number of people"})
