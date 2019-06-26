# PubSubBCM4Java

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/94dc52c7545146f4beddcb67687c7771)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nadirbelarouci/PubSubBCM4Java&amp;utm_campaign=Badge_Grade)


A publication / subscription communication system is a system for creating, sending, receiving and consuming messages.
The main part of the system is the broker who manages the transmission of messages between its clients. Clients can be either:

A Publisher who creates and publishes messages.
A Subscriber who subscribes, receives messages, and consumes them.
The publication / subscription model is a model of multiplicity 1: N, i.e a message published by a Publisher can be received by N Subscribers.

In this model the Publishers do not know the Subscribers of their messages nor the Subscribers know the Publisher of the messages that they are receiving.

The broker is the link between the Publishers and the Subscribers, it receives the published messages and forwards them to the Subscribers. The broker must guarantees the delivery of the messages and the fact that each Subscriber receives each of the messages that he receives only once.

Publication and subscription are organized around topics: A Publisher posts messages on topics and a Subscriber subscribes to a topic on which it will receives message. When Subscribers subscribe, they can provide a filter allowing to specify among the messages published on the subject to which they subscribe which interests them.
