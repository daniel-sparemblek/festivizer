from sqlalchemy import Column, Integer, String
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
    name = Column(String(50), unique=True)
    
class Event(Base)
    __tablename__ = 'event'
    
    festival_id = Column(Integer, primary_key=True)
    organizer_id = Column(Integer, primary_key=True)
    name = Column(String(50), primary_key=True)
    desc = Column(String(250))
    location = Column(Integer, primary_key=True)
    start_time = Column(DateTime, primary_key=True)
    end_time = Column(DateTime)



engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.create_all(engine)


