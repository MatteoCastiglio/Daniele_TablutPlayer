package Daniele.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import Daniele.ai.*;
import Daniele.minmaxprinter.MinMaxPrinter;
import Daniele.minmaxprinter.PrintMode;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class ClientDaniele extends TablutClient{


	//private AlphaBetaPruning ab = null;							//scommenta per AlphaBetaPruning
	private AIGame ai = null;										//usa AIGameSingleThread o AIGameP
	private final int OPENING_COUNTER = 0;
	private final int STARTING_DEPTH = 5;
	private final int MAX_DEPTH = 6;
	private Set<State> pastStates = new HashSet<State>();
	//@Matteo
	private int nwhites =0;
	private int nblacks = 0;
	private int coord[] = new int[2];
	private int whitesMoved = 0;

	
	public ClientDaniele(String player) throws  IOException {
		super(player, "Daniele");
		//ab = new AlphaBetaPruning();									//scommenta per AlphaBetaPrunin
		ai = new AIGameSingleThread(60000,MinMaxPrinter.getPrinter(PrintMode.Simple),true,true,true);	//con -1 non c'è limite di tempo	//usa AIGameSingleThread o AIGameP
	}


	private void setup()
	{

		nwhites =0;
		nblacks = 0;
		whitesMoved = 0;
		for(int i =0; i< 9; i++)
			for(int j =0; j< 9; j++)
			{if(currentState.getPawn(i, j).equals(Pawn.WHITE))
				nwhites++;
			else if( currentState.getPawn(i, j).equals(Pawn.BLACK))
				nblacks++;
			else if(currentState.getPawn(i, j).equals(Pawn.KING)) {coord[0]=i; coord[1]=j;}

				if(i == 4 && 1<j && j<7 || j==4 && 1<j && i<7)
					if(!currentState.getPawn(i, j).equals(Pawn.WHITE))
						whitesMoved++;

			}
	}

	@Override
	public void run() {
		
	/* generalizzazione per altri giochi */
		
		try {
			//comunica nome al server
			this.declareName();

			//lettura stato attuale server
			this.read();
			
			//INIZIO PARTITA
			if(this.getPlayer().equals(Turn.WHITE)) runWhite();
			else if(this.getPlayer().equals(Turn.BLACK)) runBlack();
		
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//@turnCounter serve per gestire le mosse di apertura
	private void runBlack() throws ClassNotFoundException, IOException {
		DanieleAction action = null;
		int turnCounter = 0;
		while(true) {	
			//legge stato corrente dal server (mossa avversario)
			this.read();
			pastStates.add(currentState);

			setup();


			if(turnCounter<OPENING_COUNTER)
			action = BlackOpening.nextMove(new TablutState(currentState,nwhites,nblacks,coord,whitesMoved), turnCounter);
			else
			//action = ab.AlphaBetaSearch(depth, new TablutState(currentState,nwhites,nblacks,coord,whitesMoved),MinMaxPrinter.getPrinter(PrintMode.Simple));			//scommenta per AlphaBetaPrunin
			action = ai.chooseBestMove(STARTING_DEPTH, MAX_DEPTH, new TablutState(currentState,nwhites,nblacks,coord,whitesMoved),pastStates);
			turnCounter++;
			//comunica l'azione al server
			this.write(new Action(DanieleAction.coord(action.getRowFrom(),action.getColumnFrom()),DanieleAction.coord(action.getRowTo(),action.getColumnTo()),Turn.BLACK));
			//legge stato corrente modificato dal server
			this.read();
		}
	}

	private void runWhite() throws ClassNotFoundException, IOException {
		DanieleAction action = null;
		int turnCounter=0;
		while(true) {
			//scelta mossa
			// @Matteo conteggio iniziale questo penso sia inevitabile, ma si fa una volta sola!!!!!
		
			
			setup();
			
				
			if(turnCounter<OPENING_COUNTER)
			action = WhiteOpening.nextMove(new TablutState(currentState,nwhites,nblacks,coord,whitesMoved), turnCounter);
			else
			//action = ab.AlphaBetaSearch(depth, new TablutState(this.getCurrentState(),nwhites,nblacks,coord,whitesMoved),MinMaxPrinter.getPrinter(PrintMode.Simple));			//scommenta per AlphaBetaPrunin
			action = ai.chooseBestMove(STARTING_DEPTH, MAX_DEPTH, new TablutState(currentState,nwhites,nblacks,coord,whitesMoved),pastStates);
			turnCounter++;
			//comunica l'azione al server
			this.write(new Action(DanieleAction.coord(action.getRowFrom(),action.getColumnFrom()),DanieleAction.coord(action.getRowTo(),action.getColumnTo()),Turn.WHITE));
			//legge stato corrente modificato dal server
			this.read();
			
			this.read();
			pastStates.add(currentState);
			//legge stato corrente dal server (mossa avversario)
		
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		int gametype = 4;
		String role = "";
		String name = "Daniele";
		// TODO: change the behavior?
		if (args.length < 1) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		} else {
			System.out.println(args[0]);
			role = (args[0]);
		}
		if (args.length == 2) {
			System.out.println(args[1]);
			gametype = Integer.parseInt(args[1]);
		}
		if (args.length == 3) {
			name = args[2];
		}
		System.out.println("Selected client: " + args[0]);


		TablutClient client = new ClientDaniele(role);
		client.run();
	}

}
