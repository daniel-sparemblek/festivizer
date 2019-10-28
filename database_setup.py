from sqlalchemy import Column, Integer, String, Boolean, DateTime, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import create_engine

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'

    id = Column(Integer, primary_key=True)
    username = Column(String(50), primary_key=True)
    password = Column(String(50), nullable=False)
    firstname = Column(String(50), nullable=False)
    lastname = Column(String(50), nullable=False)
    picture = Column(String(250), nullable=False)
    phone = Column(String(50), unique=True)
    email = Column(String(50), unique=True)
    role = Column(String(50), nullable=False)
    isPending = Column(Boolean)

class Specialization(Base):
    __tablename__ = 'specialization'

    id = Column(Integer, primary_key=True)
    name = Column(String(100), unique=True)

class Event(Base):
    __tablename__ = 'event'

    festival_id = Column(Integer, primary_key=True)
    organizer_id = Column(Integer, primary_key=True)
    name = Column(String(50), primary_key=True)
    desc = Column(String(250))
    location = Column(Integer, primary_key=True)
    start_time = Column(DateTime, primary_key=True)
    end_time = Column(DateTime)

class WorkerSpec(Base):
    __tablename__ = 'workerspec'

    worker_id = Column(Integer, primary_key=True)
    specialization_id = Column(Integer, primary_key=True)

class JobSpec(Base):
    __tablename__ = 'jobspec'

    job_id = Column(Integer, primary_key=True)
    specialization = Column(String(100), primary_key=True)

class Job(Base):
    __tablename__ = 'job'

    job_id = Column(Integer, primary_key=True)
    event_id = Column(Integer)
    worker_id = Column(Integer)
    auction_id = Column(Integer, unique=True)
    start_time = Column(DateTime)
    is_completed = Column(Boolean)

class Auction(Base):
    __tablename__ = 'auction'

    auction_id = Column(Integer, primary_key=True)
    start_time = Column(DateTime)
    end_time = Column(DateTime)

class Festival(Base):
    __tablename__ = 'festival'

    festival_id = Column(Integer, primary_key=True)
    creator_id = Column(DateTime)
    name = Column(String(100))
    desc = Column(String(250))
    logo = Column(String(250))
    duration = Column(Integer)
    active = Column(Boolean)

class Application(Base):
    __tablename__ = 'application'

    auction_id = Column(Integer, primary_key=True)
    worker_id = Column(Integer, primary_key=True)
    price = Column(Float)
    comment = Column(String(500))
    approximate_time = Column(Integer) #Time will be defined in days; hence Integer instead of DateTime
    number_of_people = Column(Integer)

engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.create_all(engine)

