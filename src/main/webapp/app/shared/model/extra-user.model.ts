import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IExtraUser {
  id?: number;
  nom?: string;
  prenom?: number;
  dateNaissance?: Moment;
  user?: IUser;
}

export class ExtraUser implements IExtraUser {
  constructor(public id?: number, public nom?: string, public prenom?: number, public dateNaissance?: Moment, public user?: IUser) {}
}
