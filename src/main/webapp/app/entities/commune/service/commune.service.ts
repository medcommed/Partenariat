import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommune, NewCommune } from '../commune.model';

export type PartialUpdateCommune = Partial<ICommune> & Pick<ICommune, 'id'>;

export type EntityResponseType = HttpResponse<ICommune>;
export type EntityArrayResponseType = HttpResponse<ICommune[]>;

@Injectable({ providedIn: 'root' })
export class CommuneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/communes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commune: NewCommune): Observable<EntityResponseType> {
    return this.http.post<ICommune>(this.resourceUrl, commune, { observe: 'response' });
  }

  update(commune: ICommune): Observable<EntityResponseType> {
    return this.http.put<ICommune>(`${this.resourceUrl}/${this.getCommuneIdentifier(commune)}`, commune, { observe: 'response' });
  }

  partialUpdate(commune: PartialUpdateCommune): Observable<EntityResponseType> {
    return this.http.patch<ICommune>(`${this.resourceUrl}/${this.getCommuneIdentifier(commune)}`, commune, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommune>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommune[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommuneIdentifier(commune: Pick<ICommune, 'id'>): number {
    return commune.id;
  }

  compareCommune(o1: Pick<ICommune, 'id'> | null, o2: Pick<ICommune, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommuneIdentifier(o1) === this.getCommuneIdentifier(o2) : o1 === o2;
  }

  addCommuneToCollectionIfMissing<Type extends Pick<ICommune, 'id'>>(
    communeCollection: Type[],
    ...communesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const communes: Type[] = communesToCheck.filter(isPresent);
    if (communes.length > 0) {
      const communeCollectionIdentifiers = communeCollection.map(communeItem => this.getCommuneIdentifier(communeItem)!);
      const communesToAdd = communes.filter(communeItem => {
        const communeIdentifier = this.getCommuneIdentifier(communeItem);
        if (communeCollectionIdentifiers.includes(communeIdentifier)) {
          return false;
        }
        communeCollectionIdentifiers.push(communeIdentifier);
        return true;
      });
      return [...communesToAdd, ...communeCollection];
    }
    return communeCollection;
  }
}
