import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConvention, NewConvention } from '../convention.model';

export type PartialUpdateConvention = Partial<IConvention> & Pick<IConvention, 'id'>;

type RestOf<T extends IConvention | NewConvention> = Omit<T, 'dateDebutConv'> & {
  dateDebutConv?: string | null;
};

export type RestConvention = RestOf<IConvention>;

export type NewRestConvention = RestOf<NewConvention>;

export type PartialUpdateRestConvention = RestOf<PartialUpdateConvention>;

export type EntityResponseType = HttpResponse<IConvention>;
export type EntityArrayResponseType = HttpResponse<IConvention[]>;

@Injectable({ providedIn: 'root' })
export class ConventionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conventions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(convention: NewConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(convention);
    return this.http
      .post<RestConvention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(convention: IConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(convention);
    return this.http
      .put<RestConvention>(`${this.resourceUrl}/${this.getConventionIdentifier(convention)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(convention: PartialUpdateConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(convention);
    return this.http
      .patch<RestConvention>(`${this.resourceUrl}/${this.getConventionIdentifier(convention)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConvention>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConvention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConventionIdentifier(convention: Pick<IConvention, 'id'>): number {
    return convention.id;
  }

  compareConvention(o1: Pick<IConvention, 'id'> | null, o2: Pick<IConvention, 'id'> | null): boolean {
    return o1 && o2 ? this.getConventionIdentifier(o1) === this.getConventionIdentifier(o2) : o1 === o2;
  }

  addConventionToCollectionIfMissing<Type extends Pick<IConvention, 'id'>>(
    conventionCollection: Type[],
    ...conventionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conventions: Type[] = conventionsToCheck.filter(isPresent);
    if (conventions.length > 0) {
      const conventionCollectionIdentifiers = conventionCollection.map(conventionItem => this.getConventionIdentifier(conventionItem)!);
      const conventionsToAdd = conventions.filter(conventionItem => {
        const conventionIdentifier = this.getConventionIdentifier(conventionItem);
        if (conventionCollectionIdentifiers.includes(conventionIdentifier)) {
          return false;
        }
        conventionCollectionIdentifiers.push(conventionIdentifier);
        return true;
      });
      return [...conventionsToAdd, ...conventionCollection];
    }
    return conventionCollection;
  }

  protected convertDateFromClient<T extends IConvention | NewConvention | PartialUpdateConvention>(convention: T): RestOf<T> {
    return {
      ...convention,
      dateDebutConv: convention.dateDebutConv?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restConvention: RestConvention): IConvention {
    return {
      ...restConvention,
      dateDebutConv: restConvention.dateDebutConv ? dayjs(restConvention.dateDebutConv) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConvention>): HttpResponse<IConvention> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConvention[]>): HttpResponse<IConvention[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
