import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITranche } from '../tranche.model';
import { TrancheService } from '../service/tranche.service';

@Injectable({ providedIn: 'root' })
export class TrancheRoutingResolveService implements Resolve<ITranche | null> {
  constructor(protected service: TrancheService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITranche | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tranche: HttpResponse<ITranche>) => {
          if (tranche.body) {
            return of(tranche.body);
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
