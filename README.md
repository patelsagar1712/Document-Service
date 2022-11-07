# Document Service 

#PART 1

1) Login of staff.
2) Upload of scanned documents and classified as Document Type (Personal, Address,..). Below
details need to be kept as well:
 File, File size, Filename, Unique ID, Timestamp, customer ID, Document Type, Status
3) The file details are saved with a status ‘Completed’ and we need to allow reading of the
document database every day, each one hour based on this status.
Once the job reads the db record, it should generate the file on a specific folder. That folder
path can change over time.
4) To be able to identify each document, an index file is needed. For each one document
generation, an index file with a unique name (DS+Timestamp) will be generated equally and
have below format.
This file shall contain a generic comment, Timestamp, customer_id, document_type, filename.

#PART 2

Once the file and the corresponding index file has been generated, you need to upload them to a FTP
server without a manual upload process.
On top of Spring Security, you are requested to design and implement a more robust security
mechanism.
