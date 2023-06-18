import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 27363,
  libelleRegionAr: 'back-end Practical',
};

export const sampleWithPartialData: IRegion = {
  id: 13325,
  libelleRegionAr: 'Assistant Soft',
  libelleRegionFr: 'instruction Metal salmon',
};

export const sampleWithFullData: IRegion = {
  id: 89693,
  libelleRegionAr: 'Bedfordshire',
  libelleRegionFr: 'Senior Administrator Grocery',
};

export const sampleWithNewData: NewRegion = {
  libelleRegionAr: 'ROI',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
