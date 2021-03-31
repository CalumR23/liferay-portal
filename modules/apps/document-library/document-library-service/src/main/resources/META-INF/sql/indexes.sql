create index IX_D5ED40C5 on DLFileVersionPreview (fileEntryId, ctCollectionId);
create unique index IX_DA3FFE on DLFileVersionPreview (fileEntryId, fileVersionId, ctCollectionId);
<<<<<<< HEAD
create index IX_3A1CF42B on DLFileVersionPreview (fileVersionId, ctCollectionId);

create unique index IX_1214035D on DLStorageQuota (companyId);
=======
create index IX_3A1CF42B on DLFileVersionPreview (fileVersionId, ctCollectionId);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
