import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExtraUser } from 'app/shared/model/extra-user.model';

type EntityResponseType = HttpResponse<IExtraUser>;
type EntityArrayResponseType = HttpResponse<IExtraUser[]>;

@Injectable({ providedIn: 'root' })
export class ExtraUserService {
  public resourceUrl = SERVER_API_URL + 'api/extra-users';

  constructor(protected http: HttpClient) {}

  create(extraUser: IExtraUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extraUser);
    return this.http
      .post<IExtraUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(extraUser: IExtraUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extraUser);
    return this.http
      .put<IExtraUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExtraUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExtraUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(extraUser: IExtraUser): IExtraUser {
    const copy: IExtraUser = Object.assign({}, extraUser, {
      dateNaissance: extraUser.dateNaissance && extraUser.dateNaissance.isValid() ? extraUser.dateNaissance.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? moment(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((extraUser: IExtraUser) => {
        extraUser.dateNaissance = extraUser.dateNaissance ? moment(extraUser.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
