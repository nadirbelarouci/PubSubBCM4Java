# PubSubBCM4Java

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/94dc52c7545146f4beddcb67687c7771)](https://app.codacy.com/project/nadirbelarouci/PubSubBCM4Java/dashboard?token=XQ5KcrU3OtkMoGV)


A publication / subscription communication system is a system for creating, sending, receiving and consuming messages.
The main part of the system is the broker who manages the transmission of messages between its clients. Clients can be either:

- A Publisher who creates and publishes messages.
- A Subscriber who subscribes, receives messages, and consumes them.

The publication / subscription model is a model of multiplicity 1: N, i.e a message published by a Publisher can be received by N Subscribers.

In this model the Publishers do not know the Subscribers of their messages nor the Subscribers know the Publisher of the messages that they are receiving.

The broker is the link between the Publishers and the Subscribers, it receives the published messages and forwards them to the Subscribers. The broker must guarantees the delivery of the messages and the fact that each Subscriber receives each of the messages that he receives only once.

Publication and subscription are organized around topics: A Publisher posts messages on topics and a Subscriber subscribes to a topic on which it will receives message. When Subscribers subscribe, they can provide a filter allowing to specify among the messages published on the subject to which they subscribe which interests them.

A PubSub is the heart of the publication/subscription system.
Its main role is to route requests from Publishers and Subscribers to its Broker, It implements all the methods of the PubSubService interface and delegates their implementations to its Broker. The component offers PubSubService.Offered interface.

The PubSub is also responsible of creating Subscription instances for each subscription, each Subscription instance will have a PubSubOutBoundPort instance which is connected to a SubscriberInBoundPort so it can notify the subscriber with a message, thus the PubSub component requires a Observer.Required interace.

Using the PubSub in a CVM: The PubSub offers a common PubSub instance with a default configuration, users of this component can initialize it by calling the newCommonPubSub(AbstractCVM) method, the class also offers builder methods to manually instantiate and configure the PubSub component. Example of using the common PubSub in a CVM:
```java
      PubSub.newCommonPubSub(someCVM);

      publisher = Publisher.newBuilder(someCVM).build();
      subscriber = Subscriber.newBuilder(someCVM).build();
      subscriber.subscribe(someTopic);

      Message msg = Message.newBuilder(someTopic)
          .setContent("Hello from publisher")
          .build();

      publisher.publish(msg);
```
Example of using a new configured PubSub in a CVM:
```java
    PubSub pubSub = PubSub.newBuilder(someCVM)
          .setPubSubInBoundPortURI(someURI)
          .setPublishingParallelism(5)
          .setSubscribingParallelism(5)
          .build();

      publisher = Publisher.newBuilder(someCVM,someURI).build();
      subscriber = Subscriber.newBuilder(someCVM,someURI).build();
      subscriber.subscribe(someTopic);

      Message msg = Message.newBuilder(someTopic)
          .setContent("Hello from publisher")
          .build();

      publisher.publish(msg);
```
Using the PubSub in a Distributed CVM:

The common PubSub component can be used also for a Distributed CVM, however bear in mind that in a Distributed CVM only one instance of a PubSub component among all the hosts is required, and a PubSubNode instance for each host, each PubSubNode must connect to this one unique PubSub component, hence, all the PubSub component clients are basically PubSubNode components.

Note that the Publisher components and the Subscriber components will use the PubSubNode in-bound port to establish the connection to their out-bound ports, bear in mind that they use the PubSubNode in-bound port and NOT the PubSub in-bound port to establish the connection, since the PubSubNode is already connected to the PubSubin-bound port.

Example of using the common PubSub in Distributed a CVM:
```java
      // in the DistributedCVM instantiateAndPublish method
      public void instantiateAndPublish() throws Exception {
          PubSub.newCommonPubSub(this, thisJVMURI.equals(someHost));
          if (thisJVMURI.equals(someHost)) {

              publisher = Publisher.newBuilder(this).build();

          } else if (thisJVMURI.equals(someOtherHost)) {

              subscriber = Subscriber.newBuilder(this).build();
         }

         super.instantiateAndPublish();
      }

      public void start() throws Exception {
          if (thisJVMURI.equals(someHost)) {
              Message msg = Message.newBuilder(someTopic)
                  .setContent("Hello")
                  .build();

              publisher.publish(msg);
         } else if (thisJVMURI.equals(someOtherHost)) {

              subscriber.subscribe(someTopic);

         }
          super.start();
      }
```  
Example of using a new configured PubSub in a Distributed CVM:
```java
      // in the DistributedCVM instantiateAndPublish method
      public void instantiateAndPublish() throws Exception {
          if (thisJVMURI.equals(someHost)) {
              // create only one PubSub component with in-port URI = someURI
              PubSub pubSub = PubSub.newBuilder(this)
                      .setPubSubInBoundPortURI(someURI)
                      .build();

              pubSubNode = PubSubNode.newBuilder(this, someURI).build();

              // the publisher now connects to this PubSubNode, if you don't specify
              // its in-bound URI then an NullPointerException will be thrown since
              // it will try to connect to the common PubSubNode which is not initialized at all.
              publisher = Publisher.newBuilder(this,pubSubNode.getInBoundPortURI())
                      .build();

          } else if (thisJVMURI.equals(someOtherHost)) {
              // do NOT create an other PubSub component
              // use the PubSub component of the "someHost" machine in-bound port for this PubSubNode
              pubSubNode = PubSubNode.newBuilder(this, someURI).build();
              subscriber = Subscriber.newBuilder(this,pubSubNode.getInBoundPortURI())
                      .build();
         }

         super.instantiateAndPublish();
      }

      public void start() throws Exception {
          if (thisJVMURI.equals(someHost)) {
              Message msg = Message.newBuilder(someTopic)
                  .setContent("Hello")
                  .build();

              publisher.publish(msg);
         } else if (thisJVMURI.equals(someOtherHost)) {

              subscriber.subscribe(someTopic);

         }
          super.start();
      }
      ```
