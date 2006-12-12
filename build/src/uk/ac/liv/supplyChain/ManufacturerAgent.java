package uk.ac.liv.supplyChain;

import ec.util.Parameter;
import ec.util.ParameterDatabase;
import uk.ac.liv.auction.core.Auction;

public class ManufacturerAgent extends SupplyChainAgent {
	
	private double previousPriceInAuction[] = {};
	
	public double getPreviousPrice(int auctionIdx) { return previousPriceInAuction[auctionIdx]; }

	public void setup( ParameterDatabase parameters, Parameter base, SupplyChainRandomRobinAuction[] auction ) {
	    super.setup(parameters, base, auction);
	    previousPriceInAuction = new double[auction.length];
	}
	
	public void produce( Auction auction ) {
		display = false;
		if (display)
			System.out.println("ManufacturerAgent"+getId()+".produce make="+make+"/"+make_capacity);
		if (display)
			System.out.println("AV "+toString());
		
		//TODO: take `deliver' capacities into account
		if (super.auction[0].getAge() >= super.lastProductionStartDate + make_speed) {
			
			if ( make != 0) {
				for( int i=0 ; i < deliver.length ; i++ )
					deliver[i] += make;
				make = 0;
			}
			
			boolean canProduceANewBatch = true;
			for( int i=0 ; i < source.length ; i++ )
				if ( source[i] < make_capacity )
					canProduceANewBatch = false;
			if ( canProduceANewBatch ) {
				for( int i=0 ; i < source.length ; i++ )
					source[i] -= make_capacity;
				make = make_capacity;
				super.lastProductionStartDate = super.auction[0].getAge();
			}
			
			if (display)
				System.out.println("AP "+toString());
		}//if	
	}//simulateProduction
	
	public void requestShout( Auction auction ) {
		int auctionIdx = (int) ((SupplyChainRandomRobinAuction)auction).getId() ;
		previousPriceInAuction[auctionIdx] = ((SupplyChainRandomRobinAuction)auction).getTransactionPrice();
		
		/*if (auctionForWhichThisSourceBuys[0] == auctionIdx ) {
			System.out.println("\n\n\nUPDATE in Auction "+auctionIdx);
			for ( int i=1 ; i < deliver.length ; i++)
				
		}
		//double previousPrice = ((SupplyChainRandomRobinAuction)auction);
		//((SteiglitzStrategy)strategy).setMargin();

		System.out.println("\n\n\n");
		System.out.println(auction.getAge()+" ag"+getId()+ " ManufacturerAgent.requestShout: new price in Auction "+auctionIdx+": "+previousPriceInAuction[auctionIdx]);
		System.out.println("\n\n\n");
*/
		super.requestShout(auction);
	}//requestShout
}//class