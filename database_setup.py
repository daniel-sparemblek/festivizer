import sys
from sqlalchemy import Column, ForeignKey, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'
    
    username = Column(String(250), primary_key=True)
    password = Column(String(250), nullable=False)
    firstname = Column(String(250), nullable=False)
    lastname = Column(String(250), nullable=False)
    picture = Column(String(250), nullable=False)
    phone = Column(String(250), unique=True)
    email = Column(String(250), unique=True)
    permission = Column(String(250))

engine = create_engine('sqlite:///organizacijafestivala.db')
Base.metadata.create_all(engine)

