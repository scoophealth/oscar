update demographicPharmacy, pharmacyInfo set demographicPharmacy.pharmacyID = pharmacyInfo.recordID where demographicPharmacy.pharmacyID = pharmacyInfo.ID;

alter table pharmacyInfo drop column ID;

alter table demographicPharmacy add column preferredOrder int(10);

update demographicPharmacy set preferredOrder = 1;
