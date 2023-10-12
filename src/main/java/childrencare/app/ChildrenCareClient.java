package childrencare.app;

import java.util.concurrent.CountDownLatch;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.DrugServiceGrpc;
import proto.Drug.DrugResoponse;
import proto.Drug.Empty;
import proto.Drug.ListDrugResponse;
import proto.DrugServiceGrpc.DrugServiceStub;

public class ChildrenCareClient {
    public static void main(String[] args) throws Exception {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:6565")
            .usePlaintext()
            .build();

        DrugServiceStub stub = DrugServiceGrpc.newStub(channel);

        // Construct a request
        Empty request = Empty.newBuilder().build();

        // Use a CountDownLatch to wait for the asynchronous call to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Make an Asynchronous call. Listen to responses w/ StreamObserver
        stub.findAllDrugs(request, new StreamObserver<ListDrugResponse>() {
            public void onNext(ListDrugResponse response) {
                System.out.println(response.getDrugsCount());
            }

            public void onError(Throwable t) {
                System.out.println(t.getMessage());
                latch.countDown(); // Signal completion in case of error
            }

            public void onCompleted() {
                // Typically you'll shutdown the channel somewhere else.
                // But for the purpose of this example, we'll shutdown as soon as this request is done.
                channel.shutdownNow();
                latch.countDown(); // Signal completion
            }
        });

        // Block the main thread until the asynchronous call completes
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for gRPC call to complete.");
        }
    }
}
