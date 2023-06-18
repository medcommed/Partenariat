import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommune } from '../commune.model';
import { CommuneService } from '../service/commune.service';

@Injectable({ providedIn: 'root' })
export class CommuneRoutingResolveService implements Resolve<ICommune | null> {
  constructor(protected service: CommuneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommune | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commune: HttpResponse<ICommune>) => {
          if (commune.body) {
            return of(commune.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
