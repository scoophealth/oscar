Dispensing Feature
==

This feature is quick addition to OSCAR that allows for clinics to keep track of internally dispensed medication. This was originally developed for Hamilton Public Health and their Sexual Health clinics.

How to use
--
1) Open up a patient's RX window, and search a drug to get the prescription creation in progress. Click on more, and choose to 'dispense internally', save prescription.
2) You should now get a dispense link on the right hand column of the medication listing. 
3) Clicking on this links allows you to perform dispensing transactions on this particular prescription. You can do this until it runs out (dispensed full amount covered by rx).
4) If a prescription is expired,or filled you cannot dispense anymore..only see the dispensing history in the table.


Inventory
--

A simple inventory management system was developed in order to support this feature. It's basically made of of Products. I've created a special type called DrugProduct. The fields are:

-id (internal)
-name
-code
-location
-lot #
-expiry date
-amount

The product editor is available in the Administration module under the Data Management section. From here, products can be inputted into the system one by one, or in bulk if the values are generally the same (ie. add 100 boxes of Alesse 28 of LOT # A4534 and Expiry Date 2015-06-29).

The amount is important because it's used to calculate how many individual doses have been dispensed. So for example, if you prescribe 1 OD x30 days with 2 repeats. Then that's equivalent to 90 doses. Therefore you may only dispense up to 90 doses. 
 
NOTE: Product location has to currently be configured via the DB.

Mapping
--

There's a mapping exercise under the hood. When you prescribe a medication for internal dispensing, we wanted a way so that the corresponding product could be chosen on behalf of the user. For example, I want to prescribe Alesse 28 (DIN #: 02236975) to patients, and our clinic has these available.

DrugDispensingMapping is the table. It has to manually be setup at this point. The drug fields used in the mapping are.

-din
-duration
-duration unit
-frequency code
-quantity
-take min
-take max

and, from inventory

-product code


It's a good idea to make favorites that match these. Then you prescribe a favorite, and it matches well.

