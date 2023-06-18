import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISituationProjet } from '../situation-projet.model';
import { SituationProjetService } from '../service/situation-projet.service';

@Injectable({ providedIn: 'root' })
export class SituationProjetRoutingResolveService implements Resolve<ISituationProjet | null> {
  constructor(protected service: SituationProjetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISituationProjet | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((situationProjet: HttpResponse<ISituationProjet>) => {
          if (situationProjet.body) {
            return of(situationProjet.body);
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
