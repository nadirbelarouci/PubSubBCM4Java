package fr.sorbonne_u.components.pubsub;

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

import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.pubsub.components.PubSub;
import fr.sorbonne_u.components.pubsub.components.Publisher;
import fr.sorbonne_u.components.pubsub.components.Subscriber;

import java.util.ArrayList;
import java.util.List;


//-----------------------------------------------------------------------------

/**
 * The class <code>DistributedCVM</code> implements the multi-JVM assembly for
 * the basic client/server example.
 *
 * <p><strong>Description</strong></p>
 * <p>
 * An URI provider component defined by the class <code>URIProvider</code>
 * offers an URI creation service, which is used by an URI consumer component
 * defined by the class <code>URIConsumer</code>.
 * <p>
 * The URI provider is deployed within a JVM running an instance of the CVM
 * called <code>provider</code> in the <code>config.xml</code> file. The URI
 * consumer is deployed in the instance called <code>consumer</code>.
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 *
 * <p>Created on : 2014-01-22</p>
 *
 * @author <a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class DistributedCVM
        extends AbstractDistributedCVM {

    private static final String STL = "STL";
    private static final String SAR = "SAR";

    private Publisher STLProfessor;
    private Publisher SARProfessor;
    private List<Subscriber> STLStudents;
    private List<Subscriber> SARStudents;


    public DistributedCVM(String[] args, int xLayout, int yLayout)
            throws Exception {
        super(args, xLayout, yLayout);
    }

    public static void main(String[] args) throws Exception {
        DistributedCVM da = new DistributedCVM(args, 2, 5);
        da.startStandardLifeCycle(10000);
    }

    /**
     * do some initialisation before anything can go on.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true				// no more preconditions.
     * post	true				// no more postconditions.
     * </pre>
     *
     * @see AbstractDistributedCVM#initialise()
     */
    @Override
    public void initialise() throws Exception {
        // debugging mode configuration; comment and uncomment the line to see
        // the difference
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

        super.initialise();
        // any other application-specific initialisation must be put here

    }

    /**
     * instantiate components and publish their ports.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true				// no more preconditions.
     * post	true				// no more postconditions.
     * </pre>
     *
     * @see AbstractDistributedCVM#instantiateAndPublish()
     */
    @Override
    public void instantiateAndPublish() throws Exception {
        PubSub.newCommonPubSub(this, thisJVMURI.equals(STL));


        if (thisJVMURI.equals(STL)) {

            STLProfessor = Publisher.newBuilder(this).build();


            STLStudents = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Subscriber student = Subscriber.newBuilder(this)
                        .setSubscriberInBoundPortURI("STL" + i)
                        .build();
                STLStudents.add(student);
            }


        } else if (thisJVMURI.equals(SAR)) {

            SARProfessor = Publisher.newBuilder(this).build();

            SARStudents = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Subscriber student = Subscriber.newBuilder(this)
                        .setSubscriberInBoundPortURI("SAR" + i)

                        .build();
                SARStudents.add(student);
            }
        }
        super.instantiateAndPublish();
    }

    @Override
    public void start() throws Exception {
        if (thisJVMURI.equals(STL)) {


            Message algav = Message.newBuilder("ALGAV").setContent("NO ALGAV CLASS").build();

            STLStudents.forEach(student -> student.subscribe(Topic.of("ALGAV")));
            STLStudents.get(1).subscribe(Topic.of("NOYAU"));

            Thread.sleep(1000);
            STLProfessor.publish(algav);
        } else if (thisJVMURI.equals(SAR)) {


            Message noyau = Message.newBuilder("NOYAU").setContent("NOYAU CLASS TODAY").build();

            SARStudents.forEach(student -> student.subscribe(Topic.of("NOYAU")));
            SARStudents.get(0).subscribe(Topic.of("ALGAV"));

            Thread.sleep(1000);
            SARProfessor.publish(noyau);
        }


        super.start();
    }
}
//-----------------------------------------------------------------------------
