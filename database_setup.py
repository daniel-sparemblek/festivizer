from sqlalchemy import Column, Integer, String, Boolean, DateTime, Float, ForeignKey, UniqueConstraint
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import create_engine

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'

    user_id = Column(Integer, primary_key=True)
    username = Column(String(50), unique=True)
    password = Column(String(50), nullable=False)
    firstname = Column(String(50), nullable=False)
    lastname = Column(String(50), nullable=False)
    picture = Column(String(250), nullable=False)
    phone = Column(String(50), unique=True)
    email = Column(String(50), unique=True)
    role = Column(String(50), nullable=False)
    isPending = Column(Boolean, nullable=False)


    @property
    def serialize(self):
        """Return object data in serializeable format"""
        return {
            'user_id': self.user_id,
            'username' : self.username,
            'password' : self.password,
            'firstname' : self.firstname,
            'lastname' : self.lastname,
            'picture' : self.picture,
            'phone' : self.phone,
            'email' : self.email,
            'role' : self.role,
            'isPending' : self.isPending
        }

class Specialization(Base):
    __tablename__ = 'specialization'

    specialization_id = Column(Integer, primary_key=True)
    name = Column(String(100), unique=True)

class Event(Base):
    __tablename__ = 'event'

    event_id = Column(Integer, primary_key=True)
    festival_id = Column(Integer, ForeignKey('festival.festival_id'))
    organizer_id = Column(Integer, ForeignKey('user.user_id'), ForeignKey('festivalorganizers.organizer_id'))
    name = Column(String(50), nullable=False)
    desc = Column(String(250))
    location = Column(Integer, nullable=False)
    start_time = Column(DateTime, nullable=False)
    end_time = Column(DateTime, nullable=False)
    __table_args__ = (UniqueConstraint('festival_id', 'organizer_id', 'name', 'location', 'start_time'), )

class WorkerSpec(Base):
    __tablename__ = 'workerspec'

    worker_id = Column(Integer, ForeignKey('user.user_id'), primary_key=True)
    specialization_id = Column(Integer, ForeignKey('specialization.specialization_id'), primary_key=True)

class JobSpec(Base):
    __tablename__ = 'jobspec'

    job_id = Column(Integer, ForeignKey('job.job_id'), primary_key=True)
    specialization_id = Column(Integer, ForeignKey('specialization.specialization_id') , primary_key=True)

class Job(Base):
    __tablename__ = 'job'

    job_id = Column(Integer, primary_key=True)
    event_id = Column(Integer, ForeignKey('event.event_id'))
    worker_id = Column(Integer, ForeignKey('user.user_id'))
    auction_id = Column(Integer, ForeignKey('auction.auction_id'), unique=True)
    start_time = Column(DateTime, nullable=False)
    is_completed = Column(Boolean, nullable=False)

class Auction(Base):
    __tablename__ = 'auction'

    auction_id = Column(Integer, primary_key=True)
    start_time = Column(DateTime, nullable=False)
    end_time = Column(DateTime, nullable=False)

class Festival(Base):
    __tablename__ = 'festival'

    festival_id = Column(Integer, primary_key=True)
    creator_id = Column(DateTime, ForeignKey('user.user_id'))
    name = Column(String(100), nullable=False)
    desc = Column(String(250))
    logo = Column(String(250))
    duration = Column(Integer, nullable=False)
    active = Column(Boolean, nullable=False)

class Application(Base):
    __tablename__ = 'application'

    application_id = Column(Integer, primary_key=True)
    auction_id = Column(Integer, ForeignKey('auction.auction_id'))
    worker_id = Column(Integer, ForeignKey('user.user_id'))
    price = Column(Float, nullable=False)
    comment = Column(String(500))
    approximate_time = Column(Integer, nullable=False) #Time will be defined in days; hence Integer instead of DateTime
    number_of_people = Column(Integer, nullable=False)
    __table_args__ = (UniqueConstraint('auction_id', 'worker_id'), )

class FestivalOrganizers(Base):
    __tablename__ = 'festivalorganizers'

    festival_id = Column(Integer, ForeignKey('festival.festival_id'), primary_key=True)
    organizer_id = Column(Integer, ForeignKey('user.user_id'), primary_key=True)


engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.create_all(engine)

