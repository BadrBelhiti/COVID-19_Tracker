# RIST
RIST, short for Respiratory Illness Symptoms Tracker, is a smartphone app designed to allow for easy and effective contact-tracing.
This README discusses features that are currently implemented and also plan to be implemented in the near future.

## How RIST Works
RIST uses GPS to continiously track your smartphone's precise location, storing up to 14 days of data at a time. This allows
for a "track" to be built. When a nearby resident reports symptoms, you will receive a notification alerting you of prior close
contact. Likewise, if the symptomatic user starts feeling better, they can broadcast an alert to notify report-recipients
of their current condition. RIST records all reports for statistical analysis, which allows for more accurate warnings and
information regarding local epicenters.

## How Accurate Can RIST Be?
Given data from the CDC, WHO, and other research entities, the classification of respiratory illnesses can potentially
be determined by analyzing variables such as incubation period, categorization of symptoms, local trends, etc. Incubation
period for the user can be estimated using the time of previous contacts with symptomatic users (if exists). According to
the CDC, the seasonal flu and common cold have an incubation period of about 1-3 days. In contrast, COVID-19 has a median
incubation period of about 5 days. This difference in incubation periods combined with the identification of 
lower-respiratory symptoms and local trends can be used to screen and isolate potential individuals with COVID-19.

## False Reports
Whenever a report is received by the server, it receives a score from 0-1 indicating its likelihood of being legitimate. A
report's score is used when building statistical models containing the given report. This score is determined by a machine 
learning algorithm that assesses how well the given report would fit into local models as well as general models that 
describe the behavior of certain respiratory illnesses. The score is subject to change with the introduction of new data 
and reports.

## The Potential of RIST
With the data RIST makes available, API's can be developed to build maps of local cases and local epicenters. This will
promote public awareness, emphasize quarantine, and allow for data-driven screening.

## Data Safety and Privacy
Although RIST continiously tracks your smartphone's location data, your "track" is stored locally on your device until you
report symptoms. Upon reporting symptoms, the collection of location data is sent to the server for processing. The server
then broadcasts your track to users it determines to be susceptible. This is done by periodically taking "snapshots" of users'
average locations and using that as a metric for comparison. Whether your track and a nearby user's track come in close contact
is determined by calculations done on the user's device. Overall, the only data stored by RIST is the snapshot which consists
only of a latitude, longtitude, and altitude coordinate. This design ensures users' data is as private as possible while still
allowing RIST to perform critical services.
