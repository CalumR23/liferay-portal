<<<<<<< HEAD
create index IX_D648668A on StyleBookEntry (groupId, ctCollectionId);
create index IX_7A2DA01B on StyleBookEntry (groupId, defaultStyleBookEntry, ctCollectionId);
create index IX_2006982F on StyleBookEntry (groupId, defaultStyleBookEntry, head, ctCollectionId);
create index IX_82488F5E on StyleBookEntry (groupId, head, ctCollectionId);
create index IX_20478749 on StyleBookEntry (groupId, name[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_5035E4DD on StyleBookEntry (groupId, name[$COLUMN_LENGTH:75$], head, ctCollectionId);
create index IX_38C3C389 on StyleBookEntry (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create unique index IX_4B23F11D on StyleBookEntry (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], head, ctCollectionId);
create unique index IX_6ECDD86B on StyleBookEntry (headId, ctCollectionId);
create index IX_F989310 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], companyId, ctCollectionId);
create index IX_C24ABF64 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], companyId, head, ctCollectionId);
create index IX_A7C9C014 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_52E35A12 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], groupId, ctCollectionId);
create unique index IX_47F0BCE6 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], groupId, head, ctCollectionId);
create index IX_FDBF1968 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], head, ctCollectionId);

create index IX_B34844C2 on StyleBookEntryVersion (groupId, ctCollectionId);
create index IX_E88B42E3 on StyleBookEntryVersion (groupId, defaultStyleBookEntry, ctCollectionId);
create index IX_4DF1581 on StyleBookEntryVersion (groupId, defaultStyleBookEntry, version, ctCollectionId);
create index IX_3BC5DB81 on StyleBookEntryVersion (groupId, name[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_13634EA3 on StyleBookEntryVersion (groupId, name[$COLUMN_LENGTH:75$], version, ctCollectionId);
create index IX_68B20A51 on StyleBookEntryVersion (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);
create unique index IX_5DF6C9D3 on StyleBookEntryVersion (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], version, ctCollectionId);
create index IX_142CBE82 on StyleBookEntryVersion (groupId, version, ctCollectionId);
create index IX_39DEBFAB on StyleBookEntryVersion (styleBookEntryId, ctCollectionId);
create unique index IX_ED1779B9 on StyleBookEntryVersion (styleBookEntryId, version, ctCollectionId);
create index IX_81A333D8 on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], companyId, ctCollectionId);
create index IX_59FE682C on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], companyId, version, ctCollectionId);
create index IX_911BCC4C on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], ctCollectionId);
create index IX_A72F8CDA on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], groupId, ctCollectionId);
create unique index IX_22D9116A on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], groupId, version, ctCollectionId);
create index IX_7DF81238 on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], version, ctCollectionId);
=======
create index IX_C95383D1 on StyleBookEntry (groupId, defaultStyleBookEntry, head);
create index IX_E96FB900 on StyleBookEntry (groupId, head);
create index IX_77DDEC7F on StyleBookEntry (groupId, name[$COLUMN_LENGTH:75$], head);
create unique index IX_3EEC78BF on StyleBookEntry (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], head);
create unique index IX_B5BABC0D on StyleBookEntry (headId);
create index IX_CAEEB506 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], companyId, head);
create unique index IX_E2F3F688 on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], groupId, head);
create index IX_6DBF970A on StyleBookEntry (uuid_[$COLUMN_LENGTH:75$], head);

create index IX_3CA4E523 on StyleBookEntryVersion (groupId, defaultStyleBookEntry, version);
create index IX_7B3A245 on StyleBookEntryVersion (groupId, name[$COLUMN_LENGTH:75$], version);
create unique index IX_5F837D75 on StyleBookEntryVersion (groupId, styleBookEntryKey[$COLUMN_LENGTH:75$], version);
create index IX_9A3EB024 on StyleBookEntryVersion (groupId, version);
create unique index IX_21DCB95B on StyleBookEntryVersion (styleBookEntryId, version);
create index IX_D15CEDCE on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], companyId, version);
create unique index IX_5D7DD30C on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], groupId, version);
create index IX_D0242FDA on StyleBookEntryVersion (uuid_[$COLUMN_LENGTH:75$], version);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
