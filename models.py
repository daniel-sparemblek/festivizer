import re

from marshmallow import validates, ValidationError, fields
from passlib.hash import pbkdf2_sha256 as sha256

from run import db
from run import ma
from datetime import datetime
import sys


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
        return cls.query.filter(cls.username.like(search), cls.permission != 0).all()

    @classmethod
    def find_by_phone(cls, phone):
        return cls.query.filter_by(phone=phone).first()

    @classmethod
    def find_by_email(cls, username):
        return cls.query.filter_by(username=username).first()

    @classmethod
    def update_is_pending(cls, username, value):
        user = cls.find_by_username(username)
        user.is_pending = value
        db.session.commit()

    @classmethod
    def find_by_organizing_festival_id_pending(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(festival_id == festival_id, FestivalOrganizers.status == 1).all()

    @classmethod
    def find_by_organizing_festival_id_accepted(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(FestivalOrganizers.festival_id == festival_id, FestivalOrganizers.status == 0).all()

    @classmethod
    def find_by_organizing_festival_id(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(FestivalOrganizers.festival_id == festival_id).all()

    @classmethod
    def find_by_approved_organizing_festival_id(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(FestivalOrganizers.festival_id == festival_id, FestivalOrganizers.status == 2).all()

    @classmethod
    def find_by_not_approved_organizing_festival_id(cls, festival_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.organizer_id == cls.id) \
            .filter(FestivalOrganizers.festival_id == festival_id, FestivalOrganizers.status == 1).all()

    @classmethod
    def find_by_permission(cls, value):
        return cls.query.filter_by(permission=value).all()

    @classmethod
    def find_by_is_pending(cls, value):
        return cls.query.filter_by(is_pending=value).all()

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

    @classmethod
    def find_by_organizer_id(cls, organizer_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.festival_id == cls.festival_id) \
            .filter(FestivalOrganizers.organizer_id == organizer_id).all()

    @classmethod
    def find_by_approved_organizer_id(cls, organizer_id):
        return db.session.query(cls).join(FestivalOrganizers, FestivalOrganizers.festival_id == cls.festival_id) \
            .filter(FestivalOrganizers.organizer_id == organizer_id, FestivalOrganizers.status == 2).all()

    @classmethod
    def find_completed_by_leader_id(cls, leader_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.leader_id == leader_id, cls.end_time < now).all()

    @classmethod
    def find_pending_by_leader_id(cls, leader_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.leader_id == leader_id, cls.start_time > now).all()

    @classmethod
    def find_active_by_leader_id(cls, leader_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.leader_id == leader_id, cls.end_time > now, cls.start_time < now).all()

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

    @classmethod
    def find_by_festival_id_and_organizer_id(cls, festival_id, organizer_id):
        return cls.query.filter_by(festival_id=festival_id, organizer_id=organizer_id).first()

    @classmethod
    def delete_from_db(cls, festival_organizers_id):
        db.session.query(cls).filter(cls.festival_organizers_id == festival_organizers_id).delete()
        db.session.commit()

    @classmethod
    def update_status(cls, festival_id, organizer_id, status):
        fest_org = FestivalOrganizers.find_by_festival_id_and_organizer_id(festival_id, organizer_id)
        fest_org.status = status
        db.session.commit()

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
    def find_completed_by_festival_id(cls, festival_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.festival_id == festival_id, cls.end_time < now).all()

    @classmethod
    def find_pending_by_festival_id(cls, festival_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.festival_id == festival_id, cls.start_time > now).all()

    @classmethod
    def find_active_by_festival_id(cls, festival_id):
        now = datetime.now()
        return db.session.query(cls).filter(cls.festival_id == festival_id, cls.end_time > now, cls.start_time < now).all()

    @classmethod
    def find_by_event_id(cls, event_id):
        return cls.query.filter_by(event_id=event_id).first()

    @classmethod
    def find_by_organizer_id(cls, organizer_id):
        return cls.query.filter_by(organizer_id=organizer_id).all()

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
    def find_by_job_id(cls, job_id):
        return cls.query.filter_by(job_id=job_id).first()

    @classmethod
    def find_by_leader_id(cls, leader_id):
        return db.session.query(cls).join(JobModel, JobModel.job_id == cls.job_id) \
            .join(EventModel, EventModel.event_id == JobModel.event_id) \
            .join(FestivalModel, FestivalModel.festival_id == EventModel.festival_id) \
            .filter(FestivalModel.leader_id == leader_id).all()

    @classmethod
    def find_by_organizer_id(cls, organizer_id):
        return db.session.query(cls).join(JobModel, JobModel.job_id == cls.job_id) \
            .join(EventModel, EventModel.event_id == JobModel.event_id) \
            .filter(EventModel.organizer_id == organizer_id).all()

    @classmethod
    def find_all(cls):
        return cls.query.all()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class JobModel(db.Model):
    __tablename__ = 'jobs'

    job_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    description = db.Column(db.String, nullable=False)
    event_id = db.Column(db.Integer, db.ForeignKey('events.event_id'))
    worker_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=True)
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

    @classmethod
    def find_completed_by_worker_id(cls, worker_id):
        return cls.query.filter_by(worker_id=worker_id, is_completed=1).all()

    @classmethod
    def find_active_by_worker_id(cls, worker_id):
        return cls.query.filter_by(worker_id=worker_id, is_completed=0).all()

    @classmethod
    def find_by_organizer_id(cls, organizer_id):
        return db.session.query(cls).join(EventModel, EventModel.event_id == cls.event_id) \
            .filter(EventModel.organizer_id == organizer_id).all()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        return self.job_id

    @classmethod
    def get_all(cls):
        return cls.query.all()

    @classmethod
    def find_jobs_on_auction(cls):
        return db.session.query(cls).join(AuctionModel, AuctionModel.job_id == cls.job_id).all()

    @classmethod
    def find_jobs_not_on_auction_by_organizer_id(cls, organizer_id):
        job_auc_tuple = db.session.query(cls, AuctionModel).join(EventModel, EventModel.event_id == cls.event_id)\
            .outerjoin(AuctionModel, AuctionModel.job_id == cls.job_id)\
            .filter(EventModel.organizer_id == organizer_id).all()
        print("Ja doso do drugdje", file=sys.stderr)
        jobs = []
        for job_auc in job_auc_tuple:
            print("Ja se vrtim", file=sys.stderr)
            if job_auc[1] is None:
                print("Ja nemam aukciju", file=sys.stderr)
                jobs.append(job_auc[0])
        print("Ja se vise ne vrtim", file=sys.stderr)
        return jobs


class Application(db.Model):
    __tablename__ = 'application'

    application_id = db.Column(db.Integer, primary_key=True)
    auction_id = db.Column(db.Integer, db.ForeignKey('auctions.auction_id'))
    worker_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    price = db.Column(db.Float, nullable=False)
    comment = db.Column(db.String(500), nullable=True)
    duration = db.Column(db.Integer, nullable=False)
    people_number = db.Column(db.Integer, nullable=False)
    __table_args__ = (db.UniqueConstraint('auction_id', 'worker_id'),)

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def find_by_application_id(cls, application_id):
        return cls.query.filter_by(application_id=application_id).one()

    @classmethod
    def find_all(cls):
        return cls.query.all()

    @classmethod
    def find_by_worker_id(cls, worker_id):
        return cls.query.filter_by(worker_id=worker_id).all()

    @classmethod
    def find_by_leader_id(cls, leader_id):
        return db.session.query(cls).join(AuctionModel, AuctionModel.auction_id == cls.auction_id) \
            .join(JobModel, JobModel.job_id == AuctionModel.job_id) \
            .join(EventModel, EventModel.event_id == JobModel.event_id) \
            .join(FestivalModel, FestivalModel.festival_id == EventModel.festival_id) \
            .filter(FestivalModel.leader_id == leader_id).all()


class SpecializationModel(db.Model):
    __tablename__ = 'specializations'

    specialization_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), unique=True)

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def find_by_worker_id(cls, worker_id):
        return db.session.query(cls) \
            .join(WorkerSpecializations, WorkerSpecializations.specialization_id == cls.specialization_id).filter(
            WorkerSpecializations.worker_id == worker_id).all()

    @classmethod
    def find_by_username(cls, username):
        return db.session.query(cls) \
            .join(WorkerSpecializations, WorkerSpecializations.specialization_id == cls.specialization_id).join(
            UserModel, UserModel.id == WorkerSpecializations.worker_id).filter(UserModel.username == username).all()

    @classmethod
    def find_all(cls):
        return cls.query.all()

    @classmethod
    def find_by_name(cls, name):
        return cls.query.filter_by(name=name).one()

    @classmethod
    def find_by_specialization_id(cls, specialization_id):
        return cls.query.filter_by(specialization_id=specialization_id).one()

    @classmethod
    def find_by_specialization_start(cls, start):
        search = "{}%".format(start)
        return cls.query.filter(cls.name.like(search)).all()

    @classmethod
    def find_by_job_id(cls, job_id):
        return db.session.query(cls) \
            .join(JobSpecializations, JobSpecializations.specialization_id == cls.specialization_id) \
            .filter(JobSpecializations.job_id == job_id).all()


class WorkerSpecializations(db.Model):
    __tablename__ = 'worker_specializations'

    worker_specializations_id = db.Column(db.Integer, primary_key=True)
    worker_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    specialization_id = db.Column(db.Integer, db.ForeignKey('specializations.specialization_id'))

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()


class JobSpecializations(db.Model):
    __tablename__ = 'job_specializations'

    job_specializations_id = db.Column(db.Integer, primary_key=True)
    job_id = db.Column(db.Integer, db.ForeignKey('jobs.job_id'))
    specialization_id = db.Column(db.Integer, db.ForeignKey('specializations.specialization_id'))

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


class SpecializationSchema(ma.ModelSchema):
    specialization_id = fields.Integer(required=False)
    name = fields.Str(required=True, error_messages={"required": "Missing name,"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            specialization_schema = cls(many=True)
            return specialization_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = SpecializationModel


class WorkerSpecializationSchema(ma.ModelSchema):
    worker_specializations_id = fields.Integer(required=False)
    worker_id = fields.Integer(required=True, error_messages={"required": "Missing worker id"})
    specialization_id = fields.Integer(required=True, error_messages={"required": "Missing specialization id"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            specialization_schema = cls(many=True)
            return specialization_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = WorkerSpecializations


class JobSpecializationSchema(ma.ModelSchema):
    job_specializations_id = fields.Integer(required=False)
    job_id = fields.Integer(required=True, error_messages={"required": "Missing job id"})
    specialization_id = fields.Integer(required=True, error_messages={"required": "Missing specialization id"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            specialization_schema = cls(many=True)
            return specialization_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = JobSpecializations


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
    description = fields.String(required=True, error_messages={"required": "Missing description."})
    event_id = fields.Integer(required=True, error_messages={"required": "Missing event id."})
    worker_id = fields.Integer(required=False, allow_none=True)
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
    auction_id = fields.Integer(required=False)
    worker_id = fields.Integer(required=False)
    price = fields.Float(required=True, error_messages={"required": "Missing price"})
    comment = fields.String(required=False)
    duration = fields.Integer(required=True, error_messages={"required": "Missing duration"})
    people_number = fields.Integer(required=True, error_messages={"required": "Missing number of people"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            event_schema = cls(many=True)
            return event_schema.dump(value)
        return cls().dump(value)

    class Meta:
        model = Application


class User(object):
    def __init__(self, user_id, username, password, first_name, last_name, picture, phone, email, permission,
                 is_pending):
        self.user_id = user_id
        self.username = username
        self.password = password
        self.first_name = first_name
        self.last_name = last_name
        self.picture = picture
        self.phone = phone
        self.email = email
        self.permission = permission
        self.is_pending = is_pending


class Leader(User):
    def __init__(self, user_id, username, password, first_name, last_name, picture, phone, email, permission,
                 is_pending, festivals):
        super(Leader, self).__init__(user_id, username, password, first_name, last_name, picture, phone, email,
                                     permission, is_pending)
        self.festivals = festivals


class Worker(User):
    def __init__(self, user_id, username, password, first_name, last_name, picture, phone, email, permission,
                 is_pending, specializations, jobs):
        super(Worker, self).__init__(user_id, username, password, first_name, last_name, picture, phone, email,
                                     permission, is_pending)
        self.specializations = specializations
        self.jobs = jobs


class Organizer(User):
    def __init__(self, user_id, username, password, first_name, last_name, picture, phone, email, permission,
                 is_pending, festivals):
        super(Organizer, self).__init__(user_id, username, password, first_name, last_name, picture, phone, email,
                                        permission, is_pending)
        self.festivals = festivals


class Admin(object):
    def __init__(self, user):
        self.admin_id = user.id
        self.username = user.username
        self.password = user.password
        self.first_name = user.first_name
        self.last_name = user.last_name
        self.picture = user.picture
        self.phone = user.phone
        self.email = user.email
        self.permission = user.permission
        self.is_pending = user.is_pending
        self.leaders = UserModel.find_by_is_pending(1)


class FestivalOrg(object):
    def __init__(self, festival, organizer_id):
        org_fest = FestivalOrganizers.find_by_festival_id_and_organizer_id(festival.festival_id, organizer_id)
        if org_fest is None:
            self.org_status = 0
        else:
            self.org_status = org_fest.status
        self.festival_id = festival.festival_id
        self.leader_id = festival.leader_id
        self.name = festival.name
        self.desc = festival.desc
        self.logo = festival.logo
        self.start_time = festival.start_time
        self.end_time = festival.end_time
        self.status = festival.status


class LeaderSchema(ma.Schema):
    user_id = fields.Integer(required=False)
    username = fields.Str(required=True, error_messages={"required": "Missing username."})
    password = fields.Str(required=True, error_messages={"required": "Missing password"})
    first_name = fields.Str(required=True, error_messages={"required": "Missing first name."})
    last_name = fields.Str(required=True, error_messages={"required": "Missing last name."})
    picture = fields.Str(required=True, error_messages={"required": "Missing picture."})
    phone = fields.Str(required=True, error_messages={"required": "Missing phone."})
    email = fields.Str(required=True, error_messages={"required": "Missing email."})
    permission = fields.Int(required=True, error_messages={"required": "Missing permission."})
    is_pending = fields.Int(required=True, error_messages={"required": "Missing permission."})
    festivals = fields.List(fields.Nested(FestivalSchema))

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            leaders = []
            for leader in value:
                festivals = FestivalModel.find_completed_by_leader_id(leader.id)
                new_leader = Leader(
                    user_id=leader.id,
                    username=leader.username,
                    password=leader.password,
                    first_name=leader.first_name,
                    last_name=leader.last_name,
                    picture=leader.picture,
                    phone=leader.phone,
                    email=leader.email,
                    permission=leader.permission,
                    is_pending=leader.is_pending,
                    festivals=festivals
                )
                leaders.append(new_leader)
            return cls(many=True).dump(leaders)
        festivals = FestivalModel.find_completed_by_leader_id(value.id)
        leader = Leader(
            user_id=value.id,
            username=value.username,
            password=value.password,
            first_name=value.first_name,
            last_name=value.last_name,
            picture=value.picture,
            phone=value.phone,
            email=value.email,
            permission=value.permission,
            is_pending=value.is_pending,
            festivals=festivals
        )
        return cls().dump(leader)


class AdminSchema(ma.Schema):
    admin_id = fields.Integer(required=True)
    username = fields.Str(required=True, error_messages={"required": "Missing username."})
    password = fields.Str(required=True, error_messages={"required": "Missing password"})
    first_name = fields.Str(required=True, error_messages={"required": "Missing first name."})
    last_name = fields.Str(required=True, error_messages={"required": "Missing last name."})
    picture = fields.Str(required=True, error_messages={"required": "Missing picture."})
    phone = fields.Str(required=True, error_messages={"required": "Missing phone."})
    email = fields.Str(required=True, error_messages={"required": "Missing email."})
    permission = fields.Int(required=True, error_messages={"required": "Missing permission."})
    leaders = fields.List(fields.Nested(UserSchema))

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            return cls(many=True).dump(value)
        return cls().dump(value)


class WorkerSchema(ma.Schema):
    user_id = fields.Integer(required=True)
    username = fields.Str(required=True, error_messages={"required": "Missing username."})
    password = fields.Str(required=True, error_messages={"required": "Missing password"})
    first_name = fields.Str(required=True, error_messages={"required": "Missing first name."})
    last_name = fields.Str(required=True, error_messages={"required": "Missing last name."})
    picture = fields.Str(required=True, error_messages={"required": "Missing picture."})
    phone = fields.Str(required=True, error_messages={"required": "Missing phone."})
    email = fields.Str(required=True, error_messages={"required": "Missing email."})
    permission = fields.Int(required=True, error_messages={"required": "Missing permission."})
    specializations = fields.List(fields.Nested(SpecializationSchema))
    jobs = fields.List(fields.Nested(JobSchema))

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            workers = []
            for worker in value:
                specializations = SpecializationModel.find_by_worker_id(worker_id=worker.id)
                jobs = JobModel.find_completed_by_worker_id(worker_id=worker.id)
                new_worker = Worker(
                    user_id=worker.id,
                    username=worker.username,
                    password=worker.password,
                    first_name=worker.first_name,
                    last_name=worker.last_name,
                    picture=worker.picture,
                    phone=worker.phone,
                    email=worker.email,
                    permission=worker.permission,
                    is_pending=worker.is_pending,
                    specializations=specializations,
                    jobs=jobs
                )
                workers.append(new_worker)
            return cls(many=True).dump(workers)
        specializations = SpecializationModel.find_by_worker_id(worker_id=value.id)
        jobs = JobModel.find_completed_by_worker_id(worker_id=value.id)
        worker = Worker(
            user_id=value.id,
            username=value.username,
            password=value.password,
            first_name=value.first_name,
            last_name=value.last_name,
            picture=value.picture,
            phone=value.phone,
            email=value.email,
            permission=value.permission,
            is_pending=value.is_pending,
            specializations=specializations,
            jobs=jobs
        )
        return cls().dump(worker)


class OrganizerSchema(ma.Schema):
    user_id = fields.Integer(required=False)
    username = fields.Str(required=True, error_messages={"required": "Missing username."})
    password = fields.Str(required=True, error_messages={"required": "Missing password"})
    first_name = fields.Str(required=True, error_messages={"required": "Missing first name."})
    last_name = fields.Str(required=True, error_messages={"required": "Missing last name."})
    picture = fields.Str(required=True, error_messages={"required": "Missing picture."})
    phone = fields.Str(required=True, error_messages={"required": "Missing phone."})
    email = fields.Str(required=True, error_messages={"required": "Missing email."})
    permission = fields.Int(required=True, error_messages={"required": "Missing permission."})
    festivals = fields.List(fields.Nested(FestivalSchema))

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            organizers = []
            for organizer in value:
                festivals = FestivalModel.find_by_approved_organizer_id(organizer_id=organizer.id)
                new_organizer = Organizer(
                    user_id=organizer.id,
                    username=organizer.username,
                    password=organizer.password,
                    first_name=organizer.first_name,
                    last_name=organizer.last_name,
                    picture=organizer.picture,
                    phone=organizer.phone,
                    email=organizer.email,
                    permission=organizer.permission,
                    is_pending=organizer.is_pending,
                    festivals=festivals
                )
                organizers.append(new_organizer)
            return cls(many=True).dump(organizers)
        festivals = FestivalModel.find_by_approved_organizer_id(organizer_id=value.id)
        organizer = Organizer(
            user_id=value.id,
            username=value.username,
            password=value.password,
            first_name=value.first_name,
            last_name=value.last_name,
            picture=value.picture,
            phone=value.phone,
            email=value.email,
            permission=value.permission,
            is_pending=value.is_pending,
            festivals=festivals
        )
        return cls().dump(organizer)


class JobApply(object):
    def __init__(self, job_id, name, description, event, worker, start_time, is_completed, specializations):
        self.job_id = job_id
        self.name = name
        self.description = description
        self.event = event
        self.worker = worker
        self.start_time = start_time
        self.is_completed = is_completed
        self.specializations = specializations


class EventApply(object):
    def __init__(self, event_id, festival, organizer, name, desc, location, start_time, end_time):
        self.event_id = event_id
        self.festival = festival
        self.organizer = organizer
        self.name = name
        self.desc = desc
        self.location = location
        self.start_time = start_time
        self.end_time = end_time


class EventApplySchema(ma.Schema):
    event_id = fields.Integer(required=False)
    festival = fields.Nested(FestivalSchema, required=True, error_messages={"required": "Missing festival."})
    organizer = fields.Nested(OrganizerSchema, required=True, error_messages={"required": "Missing organizer's id."})
    name = fields.Str(required=True, error_messages={"required": "Missing name."})
    desc = fields.Str(required=True, error_messages={"required": "Missing description."})
    location = fields.Str(required=True, error_messages={"required": "Missing location."})
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            events = []
            for event in value:
                festival = FestivalModel.find_by_festival_id(event.festival_id)
                user = UserModel.find_by_id(event.organizer_id)
                festivals = FestivalModel.find_by_organizer_id(user.id)
                organizer = Organizer(user.id, user.username, user.password, user.first_name,
                                      user.last_name, user.picture, user.phone, user.email,
                                      user.permission, user.is_pending, festivals)
                event = EventApply(event.event_id, festival, organizer, event.name, event.desc, event.location,
                                   event.start_time, event.end_time)
                events.append(event)
            return cls(many=True).dump(events)
        festival = FestivalModel.find_by_festival_id(value.festival_id)
        user = UserModel.find_by_id(value.organizer_id)
        festivals = FestivalModel.find_by_organizer_id(user.id)
        organizer = Organizer(user.id, user.username, user.password, user.first_name,
                              user.last_name, user.picture, user.phone, user.email,
                              user.permission, user.is_pending, festivals)
        event = EventApply(value.event_id, festival, organizer, value.name, value.desc, value.location,
                           value.start_time, value.end_time)
        return cls().dump(event)


class JobApplySchema(ma.Schema):
    job_id = fields.Integer(required=False)
    name = fields.String(required=True, error_messages={"required": "Missing name."})
    description = fields.String(required=True, error_messages={"required": "Missing name."})
    event = fields.Nested(EventApplySchema, required=True, error_messages={"required": "Missing event."})
    worker = fields.Nested(WorkerSchema, required=True, error_messages={"required": "Missing worker id."})
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time"})
    is_completed = fields.Boolean(required=True, error_messages={"required": "Missing completion"})
    specializations = fields.List(fields.Nested(SpecializationSchema))

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            jobs = []
            for job in value:
                event = EventModel.find_by_event_id(event_id=job.event_id)
                festival = FestivalModel.find_by_festival_id(event.festival_id)
                user = UserModel.find_by_id(event.organizer_id)
                festivals = FestivalModel.find_by_organizer_id(user.id)
                organizer = Organizer(user.id, user.username, user.password, user.first_name,
                                      user.last_name, user.picture, user.phone, user.email,
                                      user.permission, user.is_pending, festivals)
                event_apply = EventApply(event.event_id, festival, organizer, event.name, event.desc, event.location,
                                         event.start_time, event.end_time)
                new_worker = None
                if job.worker_id is not None:
                    worker = UserModel.find_by_id(job.worker_id)
                    specializations = SpecializationModel.find_by_worker_id(worker_id=job.worker_id)
                    jobs = JobModel.find_completed_by_worker_id(worker_id=job.worker_id)
                    new_worker = Worker(
                        user_id=worker.id,
                        username=worker.username,
                        password=worker.password,
                        first_name=worker.first_name,
                        last_name=worker.last_name,
                        picture=worker.picture,
                        phone=worker.phone,
                        email=worker.email,
                        permission=worker.permission,
                        is_pending=worker.is_pending,
                        specializations=specializations,
                        jobs=jobs
                    )
                specializations = SpecializationModel.find_by_job_id(job_id=job.job_id)
                new_job = JobApply(
                    job_id=job.job_id,
                    name=job.name,
                    description=job.description,
                    event=event_apply,
                    worker=new_worker,
                    start_time=job.start_time,
                    is_completed=job.is_completed,
                    specializations=specializations
                )
                jobs.append(new_job)
            return cls(many=True).dump(jobs)
        event = EventModel.find_by_event_id(event_id=value.event_id)
        festival = FestivalModel.find_by_festival_id(event.festival_id)
        user = UserModel.find_by_id(event.organizer_id)
        festivals = FestivalModel.find_by_organizer_id(user.id)
        organizer = Organizer(user.id, user.username, user.password, user.first_name,
                              user.last_name, user.picture, user.phone, user.email,
                              user.permission, user.is_pending, festivals)
        event_apply = EventApply(event.event_id, festival, organizer, event.name, event.desc, event.location,
                                 event.start_time, event.end_time)
        new_worker = None
        if value.worker_id is not None:
            worker = UserModel.find_by_id(value.worker_id)
            specializations = SpecializationModel.find_by_worker_id(worker_id=value.worker_id)
            jobs = JobModel.find_completed_by_worker_id(worker_id=value.worker_id)
            new_worker = Worker(
                user_id=worker.id,
                username=worker.username,
                password=worker.password,
                first_name=worker.first_name,
                last_name=worker.last_name,
                picture=worker.picture,
                phone=worker.phone,
                email=worker.email,
                permission=worker.permission,
                is_pending=worker.is_pending,
                specializations=specializations,
                jobs=jobs
            )
        specializations = SpecializationModel.find_by_job_id(job_id=value.job_id)
        new_job = JobApply(value.job_id, value.name, value.description, event_apply, new_worker,
                           value.start_time, value.is_completed, specializations)
        return cls().dump(new_job)


class FestivalOrgSchema(ma.Schema):
    festival_id = fields.Integer(required=False)
    leader_id = fields.Integer(required=True, error_messages={"required": "Missing leader."})
    name = fields.Str(required=True, error_messages={"required": "Missing festival's name."})
    desc = fields.Str(required=False)
    logo = fields.Str(required=False)
    start_time = fields.DateTime(required=True, error_messages={"required": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})
    status = fields.Integer(required=True, error_messages={"required": "Missing status."})
    org_status = fields.Integer(required=True, error_messages={"required": "Missing organizer's status."})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            return cls(many=True).dump(value)
        return cls().dump(value)


class AuctionWorker(object):
    def __init__(self, auction):
        self.auction_id = auction.auction_id
        self.job = JobModel.find_by_job_id(auction.job_id)
        self.start_time = auction.start_time
        self.end_time = auction.end_time


class ApplicationWorker(object):
    def __init__(self, application):
        self.application_id = application.application_id
        self.auction = AuctionWorker(AuctionModel.find_by_auction_id(application.auction_id))
        self.worker = UserModel.find_by_id(application.worker_id)
        self.price = application.price
        self.comment = application.comment
        self.duration = application.duration
        self.people_number = application.people_number


class AuctionWorkerSchema(ma.Schema):
    auction_id = fields.Integer(required=False)
    job = fields.Nested(JobApplySchema, required=True, error_messages={"required": "Missing job id."})
    start_time = fields.DateTime(required=True, error_messages={"required:": "Missing start time."})
    end_time = fields.DateTime(required=True, error_messages={"required": "Missing end time."})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            return cls(many=True).dump(value)
        return cls().dump(value)


class ApplicationWorkerSchema(ma.Schema):
    application_id = fields.Integer(required=False)
    auction = fields.Nested(AuctionWorkerSchema, required=False)
    worker = fields.Nested(UserSchema, required=False)
    price = fields.Float(required=True, error_messages={"required": "Missing price"})
    comment = fields.String(required=False)
    duration = fields.Integer(required=True, error_messages={"required": "Missing duration"})
    people_number = fields.Integer(required=True, error_messages={"required": "Missing number of people"})

    @classmethod
    def to_json(cls, value):
        if type(value) is list:
            return cls(many=True).dump(value)
        return cls().dump(value)
