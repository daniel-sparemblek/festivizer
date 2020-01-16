import base64
import sys
from datetime import datetime, timedelta
from run import db

from flask import request, redirect
from flask_jwt_extended import (create_access_token, create_refresh_token,
                                jwt_required, jwt_refresh_token_required, get_jwt_identity, get_raw_jwt)
from flask_restful import Resource
from sqlalchemy.exc import IntegrityError
from os import path
from decorators import permission_required
from models import (UserModel, RevokedTokenModel, UserSchema, FestivalModel, FestivalOrganizers, FestivalSchema,
                    EventModel, EventSchema, JobModel, JobSchema, AuctionModel, JobSpecializations, LeaderSchema,
                    Application, ApplicationSchema, WorkerSchema, SpecializationSchema, SpecializationModel,
                    WorkerSpecializations, OrganizerSchema, JobApplySchema, EventApplySchema, FestivalOrg,
                    FestivalOrgSchema, AuctionWorker, ApplicationWorker, ApplicationWorkerSchema, AuctionWorkerSchema,
                    Admin, AdminSchema)


class UserRegistration(Resource):
    @staticmethod
    def post():
        data = request.get_json()
        new_user_schema = UserSchema()
        validate = new_user_schema.validate(data)

        if bool(validate):
            value = list(validate.values())[0]
            return {"msg": value[0]}, 422

        if data['permission'] == 1:
            data['is_pending'] = 1
        else:
            data['is_pending'] = None

        if UserModel.find_by_username(data['username']):
            return {
                       'msg': 'User {} already exists'.format(data['username'])
                   }, 422

        if UserModel.find_by_phone(data['phone']):
            return {
                       'msg': 'Phone {} already exists'.format(data['phone'])
                   }, 422

        if UserModel.find_by_email(data['email']):
            return {
                       'msg': 'User {} already exists'.format(data['email'])
                   }, 422
        base64_message = data['picture']
        base64_bytes = base64_message.encode('ascii')
        message_bytes = base64.b64decode(base64_bytes)

        picture_path = './festivizer/images/' + data['username'] + '.png'

        f = open(picture_path, 'w+b')
        f.write(message_bytes)
        f.close()

        if data['permission'] == 1:
            new_user = UserModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['first_name'],
                last_name=data['last_name'],
                picture=picture_path,
                phone=data['phone'],
                email=data['email'],
                permission=data['permission'],
                is_pending=1
            )
        else:
            new_user = UserModel(
                username=data['username'],
                password=UserModel.generate_hash(data['password']),
                first_name=data['first_name'],
                last_name=data['last_name'],
                picture=picture_path,
                phone=data['phone'],
                email=data['email'],
                permission=data['permission'],
                is_pending=None
            )

        try:
            new_user.save_to_db()
            return {
                'msg': 'User {} was created'.format(data['username'])
            }
        except IntegrityError:
            return {'msg': 'Bad request'}, 400
        except:
            return {'msg': 'Internal server error'}, 500


class UserLogin(Resource):
    @staticmethod
    def post():
        data = request.get_json()

        if data.get('username') is None:
            return {"msg": "Missing username."}, 422

        if data.get('password') is None:
            return {"msg": "Missing password."}, 422

        current_user = UserModel.find_by_username(data['username'])

        if not current_user:
            return {'msg': 'User {} doesn\'t exist'.format(data['username'])}, 404

        if UserModel.verify_hash(data['password'], current_user.password):
            access_token = create_access_token(identity=data['username'])
            refresh_token = create_refresh_token(identity=data['username'])
            return {
                       'msg': 'Logged in as {}'.format(current_user.username),
                       'permission': current_user.permission,
                       'access_token': access_token,
                       'refresh_token': refresh_token
                   }, 200
        else:
            return {'msg': 'Wrong credentials'}, 400


class User(Resource):
    @jwt_required
    def get(self, username):
        user = UserModel.find_by_username(username)
        if user:
            return UserSchema.to_json(user)
        else:
            return {"msg": "The requested URL /{} was not found on this server.".format(username)}, 404


class Users(Resource):
    @jwt_required
    def get(self):
        if len(list(request.args)) == 0:
            return UserSchema.to_json(UserModel.return_all())

        permission = request.args.get('permission')
        is_pending = request.args.get('is_pending')

        if permission and is_pending and len(list(request.args)) == 2:
            users = UserModel.query.filter_by(permission=permission, is_pending=is_pending).all()
            return UserSchema.to_json(users)
        else:
            return redirect("https://kaogrupa.pythonanywhere.com/users")

    @jwt_required
    def put(self):
        data = request.get_json()
        username = data['username']
        decision = data['decision']
        UserModel.update_is_pending(username, decision)
        return {'msg': 'Successfully updated!'}, 200


class Leaders(Resource):
    @jwt_required
    def get(self):
        if len(list(request.args)) == 0:
            leaders = UserModel.find_by_permission(1)
            return LeaderSchema.to_json(leaders)

        username = request.args.get('username')
        if username:
            leader = UserModel.find_by_username(username=username)
            if leader is not None and leader.permission == 1:
                return LeaderSchema.to_json(leader)
        return {}, 200


class Workers(Resource):
    @jwt_required
    def get(self):
        if len(list(request.args)) == 0:
            workers = UserModel.find_by_permission(3)
            return WorkerSchema.to_json(workers)

        username = request.args.get('username')
        worker_id = request.args.get('worker_id')

        if username:
            worker = UserModel.find_by_username(username=username)
            if worker is not None and worker.permission == 3:
                return WorkerSchema.to_json(worker)

        if worker_id:
            worker = UserModel.find_by_id(worker_id)
            if worker is not None and worker.permission == 3:
                return WorkerSchema.to_json(worker)
        return {}, 200


class OnAuction(Resource):
    @jwt_required
    def post(self):
        data = request.get_json()

        job_id = data['job_id']
        jobs = JobModel.find_jobs_on_auction_by_job_id(job_id)

        if len(jobs) == 0:
            return {'is_on_auction': False}
        return {'is_on_auction': True}


class Organizers(Resource):
    @jwt_required
    def get(self):
        if len(list(request.args)) == 0:
            organizers = UserModel.find_by_permission(2)
            return OrganizerSchema.to_json(organizers)

        username = request.args.get('username')
        festival_id = request.args.get('festival_id')
        festival_id_all = request.args.get('festival_id_all')

        if username:
            organizer = UserModel.find_by_username(username=username)
            if organizer is not None and organizer.permission == 2:
                return OrganizerSchema.to_json(organizer)

        if festival_id:
            organizers = UserModel.find_by_approved_organizing_festival_id(festival_id)
            if organizers is not None:
                return OrganizerSchema.to_json(organizers)

        if festival_id_all:
            organizers = UserModel.find_by_not_approved_organizing_festival_id(festival_id_all)
            if organizers is not None:
                return OrganizerSchema.to_json(organizers)
        return {}, 200


class Admins(Resource):
    @jwt_required
    @permission_required(0)
    def get(self):
        username = get_jwt_identity()

        admin = UserModel.find_by_username(username)
        real_admin = Admin(admin)
        return AdminSchema.to_json(real_admin)


class Specializations(Resource):
    @jwt_required
    @permission_required(3)
    def post(self):
        data = request.get_json()

        specialization_schema = SpecializationSchema()
        validate = specialization_schema.validate(data)

        if bool(validate):
            value = list(validate.values())[0]
            return {"msg": value[0]}, 422

        new_specialization = SpecializationModel(
            name=data['name']
        )

        try:
            id = new_specialization.save_to_db()
            print(str(id), file=sys.stderr)

            return {'msg': 'Specialization {} was successfully created!'.format(data['name'])}, 200
        except IntegrityError:
            return {'msg': 'Bad request'}, 400
        except:
            return {'msg': 'Internal server error'}, 500

    @jwt_required
    def get(self):
        username = request.args.get('username')

        if username:
            specializations = SpecializationModel.find_by_username(username=username)
            return SpecializationSchema.to_json(specializations)

        return SpecializationSchema(many=True).dump(SpecializationModel.find_all()), 200


class SpecializationAdd(Resource):
    @jwt_required
    def post(self, specialization_id):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username=username)

        if user.permission == 3:
            worker_spec = WorkerSpecializations(
                worker_id=user.id,
                specialization_id=specialization_id
            )
            worker_spec.save_to_db()
            specialization = SpecializationModel.find_by_specialization_id(specialization_id)
            return {'msg': 'Specialization {} was successfully added to {}.'.format(specialization.name, username)}, 200
        return {'msg': 'Only workers can add specializations!'}, 403


class Festivals(Resource):
    @jwt_required
    def get(self):
        leader_id = request.args.get('leader_id')
        organizer_id = request.args.get('organizer_id')
        organizer_id_all = request.args.get('organizer_id_all')

        if len(list(request.args)) == 1 and leader_id:
            festivals = FestivalModel.find_by_leader_id(leader_id)
            return FestivalSchema.to_json(festivals)

        if organizer_id:
            festivals = FestivalModel.find_by_organizer_id(organizer_id)
            return FestivalSchema.to_json(festivals)

        festivals = FestivalModel.return_all()
        return FestivalSchema.to_json(festivals)


class FestivalsComplete(Resource):
    @jwt_required
    def get(self):
        leader_id = request.args.get('leader_id')

        if len(list(request.args)) == 1 and leader_id:
            festivals = FestivalModel.find_completed_by_leader_id(leader_id)
            return FestivalSchema.to_json(festivals)

        festivals = FestivalModel.return_all()
        return FestivalSchema.to_json(festivals)


class FestivalsActive(Resource):
    @jwt_required
    def get(self):
        leader_id = request.args.get('leader_id')

        if len(list(request.args)) == 1 and leader_id:
            festivals = FestivalModel.find_active_by_leader_id(leader_id)
            return FestivalSchema.to_json(festivals)

        festivals = FestivalModel.return_all()
        return FestivalSchema.to_json(festivals)


class FestivalsPending(Resource):
    @jwt_required
    def get(self):
        leader_id = request.args.get('leader_id')

        if len(list(request.args)) == 1 and leader_id:
            festivals = FestivalModel.find_pending_by_leader_id(leader_id)
            return FestivalSchema.to_json(festivals)

        festivals = FestivalModel.return_all()
        return FestivalSchema(many=True).to_json(festivals)


class Festival(Resource):
    @jwt_required
    @permission_required(1)
    def post(self):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username)
        leader_id = user.id

        data = request.get_json()
        data['leader_id'] = leader_id

        festival_schema = FestivalSchema()
        validate = festival_schema.validate(data)

        if bool(validate):
            value = list(validate.values())[0]
            return {"msg": value[0]}, 422

        start_time = datetime.strptime(data['start_time'], '%Y-%m-%dT%H:%M:%S.%f%z')
        end_time = datetime.strptime(data['end_time'], '%Y-%m-%dT%H:%M:%S.%f%z')

        if end_time < start_time:
            return {"msg": "End time can't be before start time."}

        base64_message = data['logo']
        base64_bytes = base64_message.encode('ascii')
        message_bytes = base64.b64decode(base64_bytes)

        i = 1
        while True:
            picture_path = './festivizer/logos/' + str(i) + '.png'
            print("jebemti indijance", file=sys.stderr)
            if not path.exists(picture_path):
                break
            i+=1

        f = open(picture_path, 'w+b')
        f.write(message_bytes)
        f.close()

        new_festival = FestivalModel(
            leader_id=data['leader_id'],
            name=data['name'],
            desc=data['desc'],
            logo=picture_path,
            start_time=start_time,
            end_time=end_time,
            status=data['status']
        )

        try:
            new_festival.save_to_db()
            return {'msg': 'Festival {} was successfully created!'.format(data['name'])}, 200
        except IntegrityError as e:
            return {'msg': 'Bad request'}, 400
        except:
            return {'msg': 'Internal server error'}, 500


class FestivalUnique(Resource):
    @jwt_required
    def get(self, festival_id):
        festival = FestivalModel.find_by_festival_id(festival_id=festival_id)
        return FestivalSchema.to_json(festival)


class FestivalApply(Resource):
    @jwt_required
    def post(self, festival_id):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username=username)
        festival_organizers = FestivalOrganizers(
            festival_id=festival_id,
            organizer_id=user.id,
            status=1
        )
        FestivalOrganizers.save_to_db(festival_organizers)
        festival = FestivalModel.find_by_festival_id(festival_id=festival_id)
        return {'msg': '{} successfully applied to {} festival.'.format(username, festival.name)}


class FestivalRevoke(Resource):
    @jwt_required
    def post(self, festival_id):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username=username)
        fest_org = FestivalOrganizers.find_by_festival_id_and_organizer_id(festival_id, user.id)

        if fest_org.status == 1:
            FestivalOrganizers.delete_from_db(fest_org.festival_organizers_id)
            festival = FestivalModel.find_by_festival_id(festival_id=festival_id)
            return {'msg': '{} successfully revoked application to {} festival.'.format(username, festival.name)}

        return {'msg': 'Can\'t revoke. You have already been accepted as organizer!!'}


class FestivalOrganizersPending(Resource):
    @jwt_required
    def get(self, festival_id):
        users = UserModel.find_by_organizing_festival_id_pending(festival_id=festival_id)
        return UserSchema.to_json(users)


class FestivalOrganizersAccepted(Resource):
    @jwt_required
    def get(self, festival_id):
        users = UserModel.find_by_organizing_festival_id_accepted(festival_id=festival_id)
        return UserSchema.to_json(users)


class FestivalOrganizersResource(Resource):
    @jwt_required
    def put(self, festival_id):
        data = request.get_json()
        decision = data['decision']
        organizer_id = data['organizer_id']
        FestivalOrganizers.update_status(festival_id, organizer_id, decision)
        return {'msg': 'Success!'}


class SearchUsers(Resource):
    @jwt_required
    def post(self):
        data = request.get_json()
        search = data['search']
        users = UserModel.find_by_username_start(search)
        return UserSchema.to_json(users)


class SearchSpecializations(Resource):
    @jwt_required
    def post(self):
        data = request.get_json()
        search = data['search']
        specializations = SpecializationModel.find_by_specialization_start(search)
        return SpecializationSchema.to_json(specializations)


class Events(Resource):
    @jwt_required
    def get(self):
        festival_id = request.args.get('festival_id')
        username = request.args.get('username')

        if len(list(request.args)) == 1 and festival_id:
            events = EventModel.find_by_festival_id(festival_id)
            return EventSchema(many=True).dump(events)

        if len(list(request.args)) == 1 and username:
            organizer = UserModel.find_by_username(username)
            return EventApplySchema.to_json(EventModel.find_by_organizer_id(organizer.id))

        print("sjebo sam" + username, file=sys.stderr)
        return {'msg:': 'Bad request'}, 400


class EventsComplete(Resource):
    @jwt_required
    def get(self):
        festival_id = request.args.get('festival_id')

        if festival_id:
            events = EventModel.find_completed_by_festival_id(festival_id)
            return EventSchema.to_json(events)


class EventsPending(Resource):
    @jwt_required
    def get(self):
        festival_id = request.args.get('festival_id')

        if festival_id:
            events = EventModel.find_pending_by_festival_id(festival_id)
            return EventSchema.to_json(events)


class EventsActive(Resource):
    @jwt_required
    def get(self):
        festival_id = request.args.get('festival_id')

        if festival_id:
            events = EventModel.find_active_by_festival_id(festival_id)
            return EventSchema.to_json(events)


class Event(Resource):
    @jwt_required
    def post(self):
        data = request.get_json()

        event_schema = EventSchema()
        validate = event_schema.validate(data)

        if bool(validate):
            value = list(validate.values())[0]
            return {"msg": value[0]}, 422

        start_time = datetime.strptime(data['start_time'], '%Y-%m-%dT%H:%M:%S.%f%z')
        end_time = datetime.strptime(data['end_time'], '%Y-%m-%dT%H:%M:%S.%f%z')

        if end_time < start_time:
            return {"msg": "End time can't be before start time."}

        find_overlapping = db.session.query(EventModel).filter(EventModel.organizer_id == data['organizer_id'], EventModel.start_time <= end_time, EventModel.end_time >= start_time).all()

        if find_overlapping:
            return {'msg': 'Organizer already has event in that time range'}, 400

        new_event = EventModel(
            festival_id=data['festival_id'],
            organizer_id=data['organizer_id'],
            name=data['name'],
            desc=data['desc'],
            location=data['location'],
            start_time=start_time,
            end_time=end_time
        )

        try:
            new_event.save_to_db()
            return {'msg': 'Event {} was successfully created!'.format(data['name'])}, 200
        except IntegrityError as e:
            return {'msg': 'Bad request'}, 400
        except Exception as e:
            return {'msg': 'Internal server error'}, 500


class EventUnique(Resource):
    @jwt_required
    def get(self, event_id):
        event = EventModel.find_by_event_id(event_id)
        return EventApplySchema.to_json(event)


class Job(Resource):
    # Kada duration na Application + start_time od posla je prosao onda je posao Completed
    @jwt_required
    def post(self):
        data = request.get_json()

        data['is_completed'] = False
        data['worker_id'] = None

        spec1 = data['spec1']
        spec2 = data['spec2']
        spec3 = data['spec3']

        spec1_id = 0
        spec2_id = 0
        spec3_id = 0
        if spec1 != "None":
            spec1_new = SpecializationModel.find_by_name(spec1)
            print("Kurac" + spec1_new.name, file=sys.stderr)
            spec1_id = spec1_new.specialization_id

        if spec2 != "None":
            spec2_id = SpecializationModel.find_by_name(spec2).specialization_id

        if spec3 != "None":
            spec3_id = SpecializationModel.find_by_name(spec3).specialization_id

        # job_schema = JobSchema()
        # validate = job_schema.validate(data)

        # if bool(validate):
        #    value = list(validate.values())[0]
        #    return {"msg": value[0]}, 422

        start_time = datetime.strptime(data['start_time'], '%Y-%m-%dT%H:%M:%S.%f%z')

        jobs = JobModel.find_by_event_id(data['event_id'])
        if not jobs:
            highest_num = 0;
        else:
            jobs.sort(key=lambda x: x.order_number, reverse=True)
            highest_num = jobs[0].order_number

        new_job = JobModel(
            name=data['name'],
            description=data['desc'],
            event_id=data['event_id'],
            worker_id=data['worker_id'],
            start_time=start_time,
            is_completed=data["is_completed"],
            comment=None,
            order_number=highest_num + 1
        )

        try:
            job_id1 = new_job.save_to_db()

            if spec1_id != 0:
                new_spec1 = JobSpecializations(
                    job_id=job_id1,
                    specialization_id=spec1_id
                )
                new_spec1.save_to_db()

            if spec2_id != 0:
                new_spec2 = JobSpecializations(
                    job_id=job_id1,
                    specialization_id=spec2_id
                )
                new_spec2.save_to_db()

            if spec3_id != 0:
                new_spec3 = JobSpecializations(
                    job_id=job_id1,
                    specialization_id=spec3_id
                )
                new_spec3.save_to_db()

            return {'msg': 'Job {} was successfully created!'.format(data['name'])}, 200
        except IntegrityError as e:
            return {'msg': 'Bad request'}, 400
        except Exception as e:
            raise e
            return {'msg': 'Internal server error'}, 500

    def get(self):
        job_id = request.args.get('job_id')
        return JobApplySchema.to_json(JobModel.find_by_job_id(job_id))


class Jobs(Resource):
    @jwt_required
    def get(self):
        Application.update_workers()

        leader_id = request.args.get('leader_id')
        job_id = request.args.get('job_id')
        username = request.args.get('username')
        is_completed = request.args.get('is_completed')
        organizer = request.args.get('organizer')

        if leader_id and not job_id:
            return JobSchema.to_json(JobModel.find_jobs_on_auction_by_leader_id(leader_id=leader_id))

        if job_id and not leader_id:
            return JobApplySchema.to_json(JobModel.find_by_job_id(job_id=job_id))

        if username is not None and is_completed == "0":
            print("NA PRAVOM MJESTU", file=sys.stderr)
            user = UserModel.find_by_username(username)
            jobs = JobModel.find_active_by_worker_id(user.id)
            print("SIZE:" + str(len(jobs)), file=sys.stderr)
            return JobApplySchema.to_json(jobs)

        if username is not None and is_completed == "1":
            user = UserModel.find_by_username(username)
            return JobApplySchema.to_json(JobModel.find_completed_by_worker_id(user.id))

        if organizer:
            user = UserModel.find_by_username(organizer)
            return JobApplySchema.to_json(JobModel.find_by_organizer_id(user.id))

        jobs = JobModel.get_all()
        return JobSchema.to_json(jobs)

    @jwt_required
    def put(self):
        event_id = request.args.get('event_id')
        jobs = JobModel.find_by_event_id(event_id)
        jobs.sort(key=lambda x: x.order_number, reverse=False)

        data = request.get_json()

        for i in range (1, len(data)+1):
            jobs[i-1].order_number = data[str(i)]

        db.session.commit()
        return {'msg': 'Success'}



class OneJob(Resource):
    @jwt_required
    def put(self, job_id):
        data = request.get_json()
        print(data['comment'], file=sys.stderr)
        comment = data['comment']
        JobModel.update_comment(job_id, comment)
        return {'msg': 'success'}

class JobsForEvent(Resource):
    @jwt_required
    def get(self):
        event_id = request.args.get('event_id')
        jobs = JobModel.find_by_event_id(event_id)
        jobs.sort(key=lambda x: x.order_number, reverse=False)
        return JobSchema.to_json(jobs)

    @jwt_required
    def put(self):
        job_id = request.args.get('job_id')
        job = JobModel.find_by_job_id(job_id)
        job.is_completed = 1
        db.session.commit()
        return {'msg': 'You have completed the job'}



class JobsNotOnAuction(Resource):
    @jwt_required
    def get(self):
        organizer = request.args.get('organizer')
        user = UserModel.find_by_username(organizer)
        print("Ja doso do ovdje", file=sys.stderr)
        return JobSchema.to_json(JobModel.find_jobs_not_on_auction_by_organizer_id(user.id))


class AvailableJobs(Resource):
    @jwt_required
    def get(self):
        return JobSchema.to_json(JobModel.find_jobs_on_auction())

    @jwt_required
    def post(self):
        data = request.get_json()
        worker_id = data['worker_id']
        auction_id = data['auction_id']

        has_application = Application.has_application(auction_id, worker_id)

        return {'has_auction': has_application}


class Auction(Resource):
    @jwt_required
    def post(self):
        data = request.get_json()

        new_auction = AuctionModel(
            job_id=data['job_id'],
            start_time=datetime.now(),
            end_time=datetime.now() + timedelta(days=1)
        )

        try:
            new_auction.save_to_db()
            job = JobModel.find_by_job_id(data['job_id'])
            return {'msg': 'Auction for {} was successfully created!'.format(job.name)}, 200
        except IntegrityError:
            return {'msg': 'Bad request'}, 400
        except Exception as e:
            raise e
            return {'msg': 'Internal server error'}, 500

    @jwt_required
    def put(self):
        data = request.get_json()
        auction_id = data['auction_id']

        auction = AuctionModel.find_by_auction_id(auction_id)
        auction.extend_auction()
        return {'msg': 'Success'}


class OrganizersFestivals(Resource):
    @jwt_required
    def get(self):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username)
        festivals = FestivalModel.return_all()

        org_festivals = []
        for festival in festivals:
            org_festivals.append(FestivalOrg(festival, user.id))

        return FestivalOrgSchema().to_json(org_festivals)


class Auctions(Resource):
    @jwt_required
    def get(self):
        leader_id = request.args.get('leader_id')
        organizer = request.args.get('organizer')

        if leader_id:
            auctions = AuctionModel.find_by_leader_id(leader_id=leader_id)
            real_auctions = []
            for auction in auctions:
                real_auctions.append(AuctionWorker(auction))
            return AuctionWorkerSchema.to_json(real_auctions)

        if organizer:
            user = UserModel.find_by_username(organizer)
            auctions = AuctionModel.find_real_by_organizer_id(user.id)

            real_auctions = []
            for auction in auctions:
                real_auctions.append(AuctionWorker(auction))

            return AuctionWorkerSchema.to_json(real_auctions)

        auctions = AuctionModel.find_all()
        real_auctions = []
        for auction in auctions:
            real_auctions.append(AuctionWorker(auction))
        return AuctionWorkerSchema.to_json(real_auctions)


class UserLogoutAccess(Resource):
    @jwt_required
    def post(self):
        jti = get_raw_jwt()['jti']
        try:
            revoked_token = RevokedTokenModel(jti=jti)
            revoked_token.add()
            return {'msg': 'Access token has been revoked.'}, 200
        except:
            return {'msg': 'Internal server error.'}, 500


class Applications(Resource):
    @jwt_required
    def post(self):
        username = get_jwt_identity()
        user = UserModel.find_by_username(username)
        data = request.get_json()
        job_id = data['job_id']
        auction = AuctionModel.find_by_job_id(job_id)

        new_application = Application(
            auction_id=auction.auction_id,
            worker_id=user.id,
            price=data['price'],
            comment=data['comment'],
            duration=data['duration'],
            people_number=data['people_number'],
            status=0
        )

        try:
            new_application.save_to_db()
            auction = AuctionModel.find_by_auction_id(auction.auction_id)
            job = JobModel.find_by_job_id(auction.job_id)
            return {'msg': 'Application for {} was successfully created!'.format(job.name)}, 200
        except IntegrityError as e:
            return {'msg': 'Bad request'}, 400
        except Exception as e:
            raise e
            return {'msg': 'Internal server error'}, 500

    @jwt_required
    def get(self):
        print("JOB ID NOBIIII1:", file=sys.stderr)
        application_id = request.args.get('application_id')
        print("JOB ID NOBIIII2:", file=sys.stderr)
        username = request.args.get('username')
        print("JOB ID NOBIIII3:", file=sys.stderr)
        leader_id = request.args.get('leader_id')
        print("JOB ID NOBIIII4:", file=sys.stderr)
        leader_id_accept = request.args.get('leader_id_accept')
        print("JOB ID NOBIIII5:", file=sys.stderr)
        job_id = request.args.get('job_id')
        print("JOB ID NOBIIII6:", file=sys.stderr)
        job_id_confirmed = request.args.get('job_id_confirmed')
        print("JOB ID NOBIIII7:", file=sys.stderr)
        job_id_unconfirmed = request.args.get('job_id_unconfirmed')
        print("JOB ID NOBIIII8:", file=sys.stderr)



        if application_id:
            return ApplicationSchema.to_json(Application.find_by_application_id(application_id=application_id))

        if username:
            user = UserModel.find_by_username(username)
            applications = Application.find_by_worker_id(user.id)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps)

        if leader_id:
            applications = Application.find_by_leader_id(leader_id)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps)

        if leader_id_accept:
            applications = Application.find_by_leader_id_accepted(leader_id_accept)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps)

        if job_id:
            print("JA SAM TU", file=sys.stderr)
            username = get_jwt_identity()
            user = UserModel.find_by_username(username)
            print(username, file=sys.stderr)
            print("ID"+str(user.id), file=sys.stderr)
            applications = Application.find_by_job_id_and_worker_id(job_id, user.id)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps[0])

        if job_id_unconfirmed:
            applications = Application.find_by_job_id_unconfirmed(job_id_unconfirmed)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps)

        if job_id_confirmed:
            applications = Application.find_by_job_id_confirmed(job_id_confirmed)

            worker_apps = []
            for app in applications:
                worker_apps.append(ApplicationWorker(app))

            return ApplicationWorkerSchema.to_json(worker_apps)

        return ApplicationSchema().to_json(Application.find_all())

    @jwt_required
    def put(self):
        data = request.get_json()
        application_id = data['application_id']
        status = data['status']

        application = Application.find_by_application_id(application_id)

        application.update_status(status)
        return {'msg': 'success'}


class UserLogoutRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        jti = get_raw_jwt()['jti']
        try:
            revoked_token = RevokedTokenModel(jti=jti)
            revoked_token.add()
            return {'message': 'Refresh token has been revoked'}
        except:
            return {'message': 'Something went wrong'}, 500


class TokenRefresh(Resource):
    @jwt_refresh_token_required
    def post(self):
        current_user = get_jwt_identity()
        access_token = create_access_token(identity=current_user)
        return {'access_token': access_token}


class Test(Resource):
    def get(self):
        leader = UserModel.find_by_username_start("")
        return LeaderSchema.to_json(leader)


class SecretResource(Resource):
    @jwt_required
    def get(self):
        return {
            'answer': 42
        }
