import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRegistration } from 'app/shared/model/registration.model';

type EntityResponseType = HttpResponse<IRegistration>;
type EntityArrayResponseType = HttpResponse<IRegistration[]>;

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  public resourceUrl = SERVER_API_URL + 'api/registrations';

  constructor(protected http: HttpClient) {}

  create(registration: IRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registration);
    return this.http
      .post<IRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(registration: IRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registration);
    return this.http
      .put<IRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(registration: IRegistration): IRegistration {
    const copy: IRegistration = Object.assign({}, registration, {
      date: registration.date && registration.date.isValid() ? registration.date.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((registration: IRegistration) => {
        registration.date = registration.date ? moment(registration.date) : undefined;
      });
    }
    return res;
  }
}
