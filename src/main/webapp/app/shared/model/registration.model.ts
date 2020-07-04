import { Moment } from 'moment';
import { ICourse } from 'app/shared/model/course.model';
import { IUser } from 'app/core/user/user.model';

export interface IRegistration {
  id?: number;
  isCertOk?: boolean;
  isPaye?: boolean;
  dossard?: number;
  temps?: number;
  date?: Moment;
  courses?: ICourse[];
  users?: IUser[];
}

export class Registration implements IRegistration {
  constructor(
    public id?: number,
    public isCertOk?: boolean,
    public isPaye?: boolean,
    public dossard?: number,
    public temps?: number,
    public date?: Moment,
    public courses?: ICourse[],
    public users?: IUser[]
  ) {
    this.isCertOk = this.isCertOk || false;
    this.isPaye = this.isPaye || false;
  }
}
