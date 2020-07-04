import { Moment } from 'moment';
import { IRegistration } from 'app/shared/model/registration.model';

export interface ICourse {
  id?: number;
  name?: number;
  description?: string;
  address?: string;
  price?: number;
  places?: number;
  dateCourse?: Moment;
  imageCourseContentType?: string;
  imageCourse?: any;
  registration?: IRegistration;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: number,
    public description?: string,
    public address?: string,
    public price?: number,
    public places?: number,
    public dateCourse?: Moment,
    public imageCourseContentType?: string,
    public imageCourse?: any,
    public registration?: IRegistration
  ) {}
}
