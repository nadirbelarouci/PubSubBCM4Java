package fr.sorbonne_u.pubsub;

//Copyright Jacques Malenfant, Sorbonne Universite.
//
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//basic component programming model to program with components
//distributed applications in the Java programming language.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability. 
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or 
//data to be ensured and,  more generally, to use and operate it in the 
//same conditions as regards security. 
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.pubsub.components.MasterPubSub;
import fr.sorbonne_u.pubsub.components.Publisher;
import fr.sorbonne_u.pubsub.components.Subscriber;

public class CVM extends AbstractCVM {


    protected MasterPubSub masterPubSub;


    protected Publisher publisher1;
    protected Publisher publisher2;
    protected Subscriber subscriber1;

    public CVM() throws Exception {
        super();
    }

    public static void main(String[] args) {
        try {
            // Create an instance of the defined component virtual machine.
            CVM a = new CVM();
            // Execute the application.
            a.startStandardLifeCycle(100000L);
            // Give some time to see the traces (convenience).
            // Simplifies the termination (termination has yet to be treated
            // properly in BCM).
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * instantiate the components, publish their port and interconnect them.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	!this.deploymentDone()
     * post	this.deploymentDone()
     * </pre>
     *
     * @see AbstractCVM#deploy()
     */
    @Override
    public void deploy() throws Exception {
        assert !this.deploymentDone();


        this.masterPubSub = new MasterPubSub();

        this.masterPubSub.toggleTracing();
        this.masterPubSub.toggleLogging();


        this.deployedComponents.add(masterPubSub);

        this.publisher1 = new Publisher();
        this.publisher1.toggleTracing();
        this.publisher1.toggleLogging();
        this.deployedComponents.add(publisher1);


        this.publisher2 = new Publisher();
        this.publisher2.toggleTracing();
        this.publisher2.toggleLogging();
        this.deployedComponents.add(publisher2);


        this.subscriber1 = new Subscriber();
        this.subscriber1.toggleTracing();
        this.subscriber1.toggleLogging();
        this.deployedComponents.add(subscriber1);


        // --------------------------------------------------------------------
        // Connection phase
        // --------------------------------------------------------------------

        this.subscriber1.doPortConnection(masterPubSub.getInBoundPortURI());
        this.publisher1.doPortConnection(masterPubSub.getInBoundPortURI());
        this.publisher2.doPortConnection(masterPubSub.getInBoundPortURI());


        super.deploy();
        assert this.deploymentDone();
    }

    @Override
    public void start() throws Exception {
        super.start();

        Message msg1 = Message.newBuilder("hello-world")
                .setContent("Hello from publisher 1")
                .build();

        subscriber1.subscribe(Topic.of("hello-world"));

        publisher1.publish(msg1);

        Message msg2 = Message.newBuilder("hello-world")
                .setContent("Hello from publisher 2")
                .build();

        publisher2.publish(msg2);

    }

    @Override
    public void shutdown() throws Exception {
        assert this.allFinalised();
        // any disconnection not done yet can be performed here

        // print logs on files, if activated
        this.publisher1.printExecutionLogOnFile("publisher1");
        this.publisher2.printExecutionLogOnFile("publisher2");
        this.masterPubSub.printExecutionLogOnFile("pubsub");

        super.shutdown();
    }
}
