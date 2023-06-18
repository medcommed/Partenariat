import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProvince, NewProvince } from '../province.model';

export type PartialUpdateProvince = Partial<IProvince> & Pick<IProvince, 'id'>;

export type EntityResponseType = HttpResponse<IProvince>;
export type EntityArrayResponseType = HttpResponse<IProvince[]>;

@Injectable({ providedIn: 'root' })
export class ProvinceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/provinces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(province: NewProvince): Observable<EntityResponseType> {
    return this.http.post<IProvince>(this.resourceUrl, province, { observe: 'response' });
  }

  update(province: IProvince): Observable<EntityResponseType> {
    return this.http.put<IProvince>(`${this.resourceUrl}/${this.getProvinceIdentifier(province)}`, province, { observe: 'response' });
  }

  partialUpdate(province: PartialUpdateProvince): Observable<EntityResponseType> {
    return this.http.patch<IProvince>(`${this.resourceUrl}/${this.getProvinceIdentifier(province)}`, province, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvince>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvince[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProvinceIdentifier(province: Pick<IProvince, 'id'>): number {
    return province.id;
  }

  compareProvince(o1: Pick<IProvince, 'id'> | null, o2: Pick<IProvince, 'id'> | null): boolean {
    return o1 && o2 ? this.getProvinceIdentifier(o1) === this.getProvinceIdentifier(o2) : o1 === o2;
  }

  addProvinceToCollectionIfMissing<Type extends Pick<IProvince, 'id'>>(
    provinceCollection: Type[],
    ...provincesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const provinces: Type[] = provincesToCheck.filter(isPresent);
    if (provinces.length > 0) {
      const provinceCollectionIdentifiers = provinceCollection.map(provinceItem => this.getProvinceIdentifier(provinceItem)!);
      const provincesToAdd = provinces.filter(provinceItem => {
        const provinceIdentifier = this.getProvinceIdentifier(provinceItem);
        if (provinceCollectionIdentifiers.includes(provinceIdentifier)) {
          return false;
        }
        provinceCollectionIdentifiers.push(provinceIdentifier);
        return true;
      });
      return [...provincesToAdd, ...provinceCollection];
    }
    return provinceCollection;
  }
}
