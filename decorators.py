from flask_jwt_extended import get_jwt_identity
from models import UserModel
from functools import wraps


def permission_required(permission):
    def function(fn):
        @wraps(fn)
        def wrapper(*args, **kwargs):
            username = get_jwt_identity()
            user = UserModel.query.filter_by(username=username).first()
            if user.permission <= permission:
                return fn(*args, **kwargs)
            else:
                return {"msg": "Permission denied."}, 403

        return wrapper

    return function
