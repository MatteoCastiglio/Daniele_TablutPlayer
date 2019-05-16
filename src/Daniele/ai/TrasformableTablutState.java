package Daniele.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;


public class TrasformableTablutState implements ITablutState {


	private State state;
	@Override
	public String toString() {
		return "TablutState" + System.lineSeparator() + state + System.lineSeparator() + " nwhites=" + nwhites + ", nblacks=" + nblacks +" king=" +coordKing[0] + " " +coordKing[1];
	}

	//private Game game;																//contiene un gioco per ogni istanza??
	private Pawn[][] board;

	private int[] coordKing;

	private int whitePawnsInFlowDirection;
	private int blackPawnsInFlowDirection;
	



	public void setNwhites(int nwhites) {
		this.nwhites = nwhites;
	}



	public void setNblacks(int nblacks) {
		this.nblacks = nblacks;
	}

	public void setCoordKing(int[] coordKing) {
		this.coordKing = coordKing;
	}

	


	// @Matteo aggiunto per controlli sui pezzi mangiati
	private  List<String> citadels =new  ArrayList<String>(Arrays.asList("a4","a5","a6","b5","d1","e1","f1","e2","i4","i5","i6","h5","d9","e9","f9","e8"));
	private int nwhites; // @Matteo numero di pezzi bianchi sulla scacchiera, mettendolo come propriet� si evita si calcolarlo dinamicamente
	private int nblacks; // @Matteo numero di pezzi neri sulla scacchiera, mettendolo come propriet� si evita si calcolarlo dinamicamente





	// this.strangeCitadels = new ArrayList<String>();

	public TrasformableTablutState(State state, int WhiteCounts,int  BlackCounts,int[] king,int whitePawnsInFlowDirection,int blackPawnsInFlowDirection) {
		this.state = state;
		//	this.game = new GameAshtonTablut(state,0, -1, "logs", "WHITE", "BLACK");
		this.board = state.getBoard();
		nwhites= WhiteCounts;
		nblacks= BlackCounts;
		coordKing = king;
		this.whitePawnsInFlowDirection=whitePawnsInFlowDirection;
		this.blackPawnsInFlowDirection = blackPawnsInFlowDirection;
	}

	@Override
	public List<Pos> trasformState(DanieleAction action) {

		// return game.getNextState(state ,action);

		return getNextState(state ,action);

	}

	@Override
	public void trasformStateBack(DanieleAction a, List<Pos> pawnsRemoved) {

 
			
		
		// return game.getNextState(state ,action);
		switch(state.getTurn()){
		case WHITEWIN :{
			state.setPawn(a.getRowFrom(), a.getColumnFrom(), Pawn.KING);
			coordKing = new int[] {a.getRowFrom(),a.getColumnFrom()};
			state.setTurn(Turn.WHITE);
			break;		}
		case BLACKWIN : {
			if(coordKing[0]<4&&a.getRowTo()<4&&!(a.getRowFrom()<4))
				blackPawnsInFlowDirection--;
				else if(coordKing[0]>4&&a.getRowTo()>4&&!(a.getRowFrom()>4))
					blackPawnsInFlowDirection--;
				if(coordKing[1]<4&&a.getColumnTo()<4&&!(a.getColumnFrom()<4))
					blackPawnsInFlowDirection--;
				else if(coordKing[1]>4&&a.getColumnTo()>4&&!(a.getColumnFrom()>4))
					blackPawnsInFlowDirection--;
			state.setPawn(a.getRowFrom(), a.getColumnFrom(), Pawn.BLACK);
			state.setTurn(Turn.BLACK);
			for(int i = 0; i< pawnsRemoved.size(); i++) {
				state.setPawn(pawnsRemoved.get(i).row, pawnsRemoved.get(i).col, Pawn.WHITE);
				nwhites++;}
			break;
		}
		case BLACK : {
			
				
				
		
			state.setTurn(Turn.WHITE);
			if(state.getPawn(a.getRowTo(),a.getColumnTo()).equals(Pawn.KING)) {
				state.setPawn(a.getRowFrom(), a.getColumnFrom(), Pawn.KING);
				coordKing = new int[] {a.getRowFrom(),a.getColumnFrom()};
			}
			
			else {state.setPawn(a.getRowFrom(), a.getColumnFrom(), Pawn.WHITE);
			if(coordKing[0]<4&&a.getRowTo()<4&&!(a.getRowFrom()<4))
				whitePawnsInFlowDirection--;
				else if(coordKing[0]>4&&a.getRowTo()>4&&!(a.getRowFrom()>4))
					whitePawnsInFlowDirection--;
				if(coordKing[1]<4&&a.getColumnTo()<4&&!(a.getColumnFrom()<4))
					whitePawnsInFlowDirection--;
				else if(coordKing[1]>4&&a.getColumnTo()>4&&!(a.getColumnFrom()>4))
					whitePawnsInFlowDirection--;
			}
			
			for(int i = 0; i< pawnsRemoved.size(); i++)
			{
				state.setPawn(pawnsRemoved.get(i).row, pawnsRemoved.get(i).col, Pawn.BLACK);
				nblacks++;	}
			break; }

		case WHITE:  {
			if(coordKing[0]<4&&a.getRowTo()<4&&!(a.getRowFrom()<4))
				blackPawnsInFlowDirection--;
				else if(coordKing[0]>4&&a.getRowTo()>4&&!(a.getRowFrom()>4))
					blackPawnsInFlowDirection--;
				if(coordKing[1]<4&&a.getColumnTo()<4&&!(a.getColumnFrom()<4))
					blackPawnsInFlowDirection--;
				else if(coordKing[1]>4&&a.getColumnTo()>4&&!(a.getColumnFrom()>4))
					blackPawnsInFlowDirection--;
			state.setPawn(a.getRowFrom(), a.getColumnFrom(), Pawn.BLACK);
			state.setTurn(Turn.BLACK);
			for(int i = 0; i< pawnsRemoved.size(); i++) {
				state.setPawn(pawnsRemoved.get(i).row, pawnsRemoved.get(i).col, Pawn.WHITE);
				nwhites++;
			}
			break;}
		//solo per completezza
		case DRAW:
			break;
		default:
			break;
		}

		state.setPawn(a.getRowTo(), a.getColumnTo(), Pawn.EMPTY);



	}

	@Override
	public List<DanieleAction> getTopLeftMoves() {
		List<DanieleAction> moves = new LinkedList<DanieleAction>();

		if(state.getTurn().equals(Turn.WHITE)) {	//MAX player
			for (int i = 0; i < this.board.length/2; i++) {
				for (int j = 0; j < this.board.length/2; j++) {
					//per ogni giocatore bianco ci si salva tutte le possibili azioni..
					if(this.board[i][j].equals(Pawn.WHITE) || this.board[i][j].equals(Pawn.KING)) {
						//..in verticale
						for(int x = i-1; x >= 0; x--)
							if(!this.board[x][j].equals(Pawn.EMPTY) || isPawnAccampamento(x, j)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							/*serve?*/				else /*if(this.board[x][j].equals(Pawn.EMPTY))*/ moves.add(new DanieleAction(i, j, x, j));
						for(int x = i+1; x < this.board.length/2; x++)
							if(!this.board[x][j].equals(Pawn.EMPTY) || isPawnAccampamento(x, j)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j,x, j)) ;
						//..in orizzontale
						for(int x = j-1; x >= 0; x--)
							if(!this.board[i][x].equals(Pawn.EMPTY) || isPawnAccampamento(i, x)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j,i, x));
						for(int x = j+1; x < this.board.length/2; x++)
							if(!this.board[i][x].equals(Pawn.EMPTY) || isPawnAccampamento(i, x)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j, i, x));
					}
				}
			}
		}
		else if(state.getTurn().equals(Turn.BLACK)) {	//MIN player
			for (int i = 0; i < this.board.length/2; i++) {
				for (int j = 0; j < this.board.length/2; j++) {
					//per ogni giocatore nero ci si salva tutte le possibili azioni..
					if(this.board[i][j].equals(Pawn.BLACK)) {
						//..in verticale
						for(int x = i-1; x >= 0; x--)
							if(!this.board[x][j].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(x, j))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else moves.add(new DanieleAction(i, j,x, j));
						for(int x = i+1; x < this.board.length/2; x++)
							if(!this.board[x][j].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(x, j))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else moves.add(new DanieleAction(i, j,x, j));
						//..in orizzontale
						for(int x = j-1; x >= 0; x--)
							if(!this.board[i][x].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(i, x))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else moves.add(new DanieleAction(i, j , i, x));
						for(int x = j+1; x < this.board.length/2; x++)
							if(!this.board[i][x].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(i, x))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else moves.add(new DanieleAction(i, j, i, x));
					}
				}	
			}
		}

		return moves;
	}

	@Override
	public List<DanieleAction> getAllLegalMoves() {
		List<DanieleAction> moves = new LinkedList<DanieleAction>();

		if(state.getTurn().equals(Turn.WHITE)) {	//MAX player
			for (int i = 0; i < this.board.length; i++) {
				for (int j = 0; j < this.board.length; j++) {
					//per ogni giocatore bianco ci si salva tutte le possibili azioni..
					if(this.board[i][j].equals(Pawn.WHITE) || this.board[i][j].equals(Pawn.KING)) {
						//..in verticale
						for(int x = i-1; x >= 0; x--)
							if(!this.board[x][j].equals(Pawn.EMPTY) || isPawnAccampamento(x, j)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							/*serve?*/				else /*if(this.board[x][j].equals(Pawn.EMPTY))*/ moves.add(new DanieleAction(i, j,x, j));
						for(int x = i+1; x < this.board.length; x++)
							if(!this.board[x][j].equals(Pawn.EMPTY) || isPawnAccampamento(x, j)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j,x, j));
						//..in orizzontale
						for(int x = j-1; x >= 0; x--)
							if(!this.board[i][x].equals(Pawn.EMPTY) || isPawnAccampamento(i, x)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j,i, x));
						for(int x = j+1; x < this.board.length; x++)
							if(!this.board[i][x].equals(Pawn.EMPTY) || isPawnAccampamento(i, x)) break; 	//non posso scavalcare o terminare su altre pedine o accampamento o castello
							else moves.add(new DanieleAction(i, j,i, x));
					}
				}
			}
		}
		else if(state.getTurn().equals(Turn.BLACK)) {	//MIN player
			for (int i = 0; i < this.board.length; i++) {
				for (int j = 0; j < this.board.length; j++) {
					//per ogni giocatore nero ci si salva tutte le possibili azioni..
					if(this.board[i][j].equals(Pawn.BLACK)) {
						//..in verticale
						for(int x = i-1; x >= 0; x--)
							if(!this.board[x][j].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(x, j))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else if((x!=0&&j!=0)&&(x!=0&&j!=8))moves.add(new DanieleAction(i, j,x, j));
						for(int x = i+1; x < this.board.length; x++)
							if(!this.board[x][j].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(x, j))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else if((x!=9&&j!=0)&&(x!=9&&j!=8))moves.add(new DanieleAction(i, j,x, j));
						//..in orizzontale
						for(int x = j-1; x >= 0; x--)
							if(!this.board[i][x].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(i, x))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else if((x!=0&&i!=0)&&(x!=0&&i!=9))moves.add(new DanieleAction(i, j,i, x));
						for(int x = j+1; x < this.board.length; x++)
							if(!this.board[i][x].equals(Pawn.EMPTY) || (!isPawnAccampamento(i, j) && isPawnAccampamento(i, x))) break; 	//non posso scavalcare o terminare su altre pedine o (accampamento) o castello
							else if((x!=9&&i!=0)&&(x!=9&&i!=9))moves.add(new DanieleAction(i, j,i, x));
					}
				}	
			}
		}
		//Collections.reverse(moves);
		return moves;
	}

	public boolean isPawnAccampamento(int i, int j) {
		int middle = (this.board.length-1)/2;

		//parti di accampamenti sui bordi
		if( (i>=middle-1 && i<=middle+1) && (j==0 || j==this.board.length-1) ) return true;
		if( (i==0 || i==this.board.length-1) && (j>=middle-1 && j<=middle+1) ) return true;
		//parti di accampamenti interni
		if( i==middle && (j==1 || j==this.board.length-2) ) return true;
		if( (i==1 || i==this.board.length-2) && j==middle ) return true;

		//trono 														(non dovrebbe servire) -> si presuppone che quando il re si muove questo pawn diventi Pawn.THRONE
		//if( i==middle && j==middle ) return true;

		return false;
	}



	@Override
	public State getState() {
		return state;
	}

	//@Matteo la gestione andrebbe fatta come per il numero delle pedine
	@Override
	public int[] getCoordKing() {

		/*
		int coord[] = new int[2];
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board.length; j++) {
				if(this.board[i][j].equals(Pawn.KING)) {coord[0]=i; coord[1]=j; return coord;}
			}
		}
		 */
		return coordKing; //dovrebbe semrpe esserci il re, altrimenti la partita è conclusa
	}



	// @Matteo questi metodi sono pi� o meno copiati dalla classe GameAshtonTablut poi vedremo meglioc ome metterli 

	//@Matteo turn nelle action non serve a nulla, potremmo farci una nostra Action


	private List<Pos> getNextState(State mystate, DanieleAction a) {

		List<Pos> p = null;
		Pawn pawn = state.getPawn(a.getRowFrom(), a.getColumnFrom());
		Pawn[][] newBoard = state.getBoard();
		// State newState = new State();
		// libero il trono o una casella qualunque
		if (a.getColumnFrom() == 4 && a.getRowFrom() == 4) {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = Pawn.THRONE;
		} else {
			newBoard[a.getRowFrom()][a.getColumnFrom()] = Pawn.EMPTY;
		}

		// metto nel nuovo tabellone la pedina mossa
		newBoard[a.getRowTo()][a.getColumnTo()] = pawn;
		// aggiorno il tabellone
		state.setBoard(newBoard);
		// cambio il turno
		if(pawn.equals(Pawn.KING))
			coordKing = new int[] {a.getRowTo(),a.getColumnTo()};
		
		if(pawn.equals(Pawn.WHITE)) {
			if(coordKing[0]<4&&a.getRowTo()<4&&!(a.getRowFrom()<4))
			whitePawnsInFlowDirection++;
			else if(coordKing[0]>4&&a.getRowTo()>4&&!(a.getRowFrom()>4))
				whitePawnsInFlowDirection++;
			if(coordKing[1]<4&&a.getColumnTo()<4&&!(a.getColumnFrom()<4))
				whitePawnsInFlowDirection++;
			else if(coordKing[1]>4&&a.getColumnTo()>4&&!(a.getColumnFrom()>4))
				whitePawnsInFlowDirection++;
			}
			
		
		if(pawn.equals(Pawn.BLACK)) {
			if(coordKing[0]<4&&a.getRowTo()<4&&!(a.getRowFrom()<4))
				blackPawnsInFlowDirection++;
			else if(coordKing[0]>4&&a.getRowTo()>4&&!(a.getRowFrom()>4))
				blackPawnsInFlowDirection++;
			if(coordKing[1]<4&&a.getColumnTo()<4&&!(a.getColumnFrom()<4))
				blackPawnsInFlowDirection++;
			else if(coordKing[1]>4&&a.getColumnTo()>4&&!(a.getColumnFrom()>4))
				blackPawnsInFlowDirection++;
			}
		
		

		


		if (state.getTurn().equals(Turn.WHITE)) {
			state.setTurn(State.Turn.BLACK);
			p = checkCaptureWhite(state,a);


		} else {
			state.setTurn(State.Turn.WHITE);
			p = checkCaptureBlack(state,a);
		}

		return p;


	}

	private List<Pos> checkCaptureBlack(State state, DanieleAction a) {


		// @Matteo tutti i controlli a prescindere si pu� fare meglio???

		List<Pos> p = new ArrayList<Pos>();
		p.addAll(this.checkCaptureBlackPawnRight(state, a));
		p.addAll(this.checkCaptureBlackPawnLeft(state, a));
		p.addAll(this.checkCaptureBlackPawnUp(state, a));
		p.addAll(this.checkCaptureBlackPawnDown(state, a));

		this.checkCaptureBlackKingRight(state, a);
		this.checkCaptureBlackKingLeft(state, a);
		this.checkCaptureBlackKingDown(state, a);
		this.checkCaptureBlackKingUp(state, a);
		return p;
	}

	private List<Pos> checkCaptureWhite(State state, DanieleAction a) {
		// controllo se mangio a destra

		List<Pos> p = new ArrayList<Pos>() ;
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))
								&& !(a.getColumnTo() + 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() + 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() + 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			nblacks=nblacks-1;
			p.add(new Pos (a.getRowTo(), a.getColumnTo() + 1));

		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
								&& !(a.getColumnTo() - 2 == 8 && a.getRowTo() == 4)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 0)
								&& !(a.getColumnTo() - 2 == 4 && a.getRowTo() == 8)
								&& !(a.getColumnTo() - 2 == 0 && a.getRowTo() == 4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			nblacks=nblacks-1;
			p.add(new Pos(a.getRowTo(), a.getColumnTo() - 1));
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() - 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() - 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() - 2 == 4)))) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
			nblacks=nblacks-1;
			p.add(new Pos (a.getRowTo() -1 , a.getColumnTo() ));
		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
						|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
								&& !(a.getColumnTo() == 8 && a.getRowTo() + 2 == 4)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 0)
								&& !(a.getColumnTo() == 4 && a.getRowTo() + 2 == 8)
								&& !(a.getColumnTo() == 0 && a.getRowTo() + 2 == 4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
			nblacks=nblacks-1;
			p.add(new Pos (a.getRowTo() + 1 , a.getColumnTo() ));
		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
				|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);

			}
		}


		return p;
	}

	private void checkCaptureBlackKingLeft(State state, DanieleAction a){
		// ho il re sulla sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")) {
				if (state.getPawn(6, 4).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() - 1).equals("f5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
		}

	}

	private void checkCaptureBlackKingRight(State state, DanieleAction a){
		// ho il re sulla destra
		if (a.getColumnTo() < state.getBoard().length - 2
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("K"))) {
			// re sul trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")) {
				if (state.getPawn(2, 4).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")) {
				if (state.getPawn(5, 5).equalsPawn("B") && state.getPawn(6, 4).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo() + 1).equals("e5")) {
				if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
		}

	}

	private void checkCaptureBlackKingDown(State state, DanieleAction a){
		// ho il re sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("K")) {

			// re sul trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(5, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")) {
				if (state.getPawn(3, 3).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(5, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() + 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
		}

	}

	private void checkCaptureBlackKingUp(State state, DanieleAction a){
		// ho il re sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("K")) {
			// re sul trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(3, 4).equalsPawn("B") && state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// re adiacente al trono
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e6")) {
				if (state.getPawn(5, 3).equalsPawn("B") && state.getPawn(5, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")) {
				if (state.getPawn(4, 2).equalsPawn("B") && state.getPawn(3, 3).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			if (state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")) {
				if (state.getPawn(4, 6).equalsPawn("B") && state.getPawn(3, 5).equalsPawn("B")) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
			// sono fuori dalle zone del trono
			if (!state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo() - 1, a.getColumnTo()).equals("e5")) {
				if (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))) {
					state.setTurn(State.Turn.BLACKWIN);

				}
			}
		}


	}

	private List<Pos> checkCaptureBlackPawnRight(State state, DanieleAction a)	{

		List<Pos> p = new ArrayList<Pos>() ;	
		if (a.getColumnTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W")) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				nwhites=nwhites-1;
				p.add(new Pos (a.getRowTo()  , a.getColumnTo() +1));
			}
			if (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				nwhites=nwhites-1;
				p.add(new Pos (a.getRowTo()  , a.getColumnTo() +1));
			}
			if (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				nwhites=nwhites-1;
				p.add(new Pos (a.getRowTo()  , a.getColumnTo() +1));
			}
			if (state.getBox(a.getRowTo(), a.getColumnTo() + 2).equals("e5")) {
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
				nwhites=nwhites-1;
				p.add(new Pos (a.getRowTo()  , a.getColumnTo() +1 ));
			}

		}

		return p;
	}

	private List<Pos> checkCaptureBlackPawnLeft(State state, DanieleAction a){
		//mangio a sinistra
		List<Pos> p = new ArrayList<Pos>() ;
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo() - 2).equals("e5"))))

		{
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
			p.add(new Pos (a.getRowTo()  , a.getColumnTo() -1));
			// @Matteo AGGIUNTA
			nwhites=nwhites-1;
		}
		return p;
	}

	private List<Pos> checkCaptureBlackPawnUp(State state, DanieleAction a){
		// controllo se mangio sopra
		List<Pos> p = new ArrayList<Pos>() ;
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() - 2, a.getColumnTo()).equals("e5"))))

		{
			state.removePawn(a.getRowTo()-1, a.getColumnTo());
			// @Matteo AGGIUNTA
			nwhites=nwhites-1;
			p.add(new Pos (a.getRowTo() -1 , a.getColumnTo() ));

		}
		return p;
	}

	private List<Pos> checkCaptureBlackPawnDown(State state, DanieleAction a){
		// controllo se mangio sotto
		List<Pos> p = new ArrayList<Pos>() ;
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo() + 2, a.getColumnTo()).equals("e5"))))
		{
			state.removePawn(a.getRowTo()+1, a.getColumnTo());
			p.add(new Pos (a.getRowTo() +1 , a.getColumnTo() ));
			// @Matteo AGGIUNTA
			nwhites=nwhites- 1;

		}
		return p;
	}


	public double getFlow()
	{
		//	4|1
		//	3|2
		
		double res=0;
		//troppo pesante
//		int n1W=0, n2W=0, n3W=0, n4W=0;
//		int n1B=0, n2B=0, n3B=0, n4B=0;
		int n1=0, n2=0, n3=0, n4=0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
//				if(this.board[i][j].equals(Pawn.BLACK)) n4B++;
//				else if(this.board[i][j].equals(Pawn.WHITE)) n4W++;
				if(!this.board[i][j].equals(Pawn.EMPTY)) n4++;
			}
			for (int j = 5; j < 9; j++) {
				if(!this.board[i][j].equals(Pawn.EMPTY)) n1++;
			}
		}
		for (int i = 5; i < 9; i++) {
			for (int j = 0; j < 4; j++) {
				if(!this.board[i][j].equals(Pawn.EMPTY)) n3++;
			}
			for (int j = 5; j < 9; j++) {
				if(!this.board[i][j].equals(Pawn.EMPTY)) n2++;
			}
		}
		
//		int asse14=0, asse12=0, asse23=0, asse34=0;
//		if(coordKing[0]==4 && coordKing[1]>=0 && coordKing[1]<=3) asse34++;
//		if(coordKing[0]==4 && coordKing[1]>=5 && coordKing[1]<=8) asse12++;
//		if(coordKing[1]==4 && coordKing[0]>=0 && coordKing[0]<=3) asse14++;
//		if(coordKing[1]==4 && coordKing[0]>=0 && coordKing[0]<=3) asse23++;
		
		int max=Math.max(n1, n2); max=Math.max(max, n3); max=Math.max(max, n4);
		if(max==n1) res=n1+(n2>n4?(n2+0.5*n4):(0.5*n2+n4));
		if(max==n2) res=n2+(n1>n3?(n1+0.5*n3):(0.5*n1+n3));
		if(max==n3) res=n3+(n2>n4?(n2+0.5*n4):(0.5*n2+n4));
		if(max==n4) res=n4+(n1>n3?(n1+0.5*n3):(0.5*n1+n3));

		return res;
		
	}

	@Override
	public int BlacksCount() {
		// TODO Auto-generated method stub
		return nblacks;
	}

	@Override
	public int WhitesCount() {
		// TODO Auto-generated method stub
		return nwhites;
	}


	@Override
	public int getPawnsOnKingDiagonal()
	{
		int res=0;
		if(coordKing[0]-1>=0)
		{
			if(coordKing[1]-1>=0)
				if(state.getPawn(coordKing[0]-1,coordKing[1]-1).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]-1,coordKing[1]-1).equals(Pawn.BLACK)) res--;
			if(coordKing[1]+1<=8)
				if(state.getPawn(coordKing[0]-1,coordKing[1]+1).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]-1,coordKing[1]+1).equals(Pawn.BLACK)) res--;
		}
		if(coordKing[0]+1<=8){
			if(coordKing[1]-1>=0)
				if(state.getPawn(coordKing[0]+1,coordKing[1]-1).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]+1,coordKing[1]-1).equals(Pawn.BLACK)) res--;
			if(coordKing[1]+1<=8)
				if(state.getPawn(coordKing[0]+1,coordKing[1]+1).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]+1,coordKing[1]+1).equals(Pawn.BLACK)) res--;
		}
		return res;
	}



	@Override
	public int getPawnsOnKingDiagonal2()
	{
		int res=0;
		if(coordKing[0]-2>=0)
		{
			if(coordKing[1]-2>=0)
				if(state.getPawn(coordKing[0]-2,coordKing[1]-2).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]-2,coordKing[1]-2).equals(Pawn.BLACK)) res--;
			if(coordKing[1]+2<=8)
				if(state.getPawn(coordKing[0]-2,coordKing[1]+2).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]-2,coordKing[1]+2).equals(Pawn.BLACK)) res--;
		}
		if(coordKing[0]+2<=8){
			if(coordKing[1]-2>=0)
				if(state.getPawn(coordKing[0]+2,coordKing[1]-2).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]+2,coordKing[1]-2).equals(Pawn.BLACK)) res--;
			if(coordKing[1]+2<=8)
				if(state.getPawn(coordKing[0]+2,coordKing[1]+2).equals(Pawn.WHITE)) res++;
				else if (state.getPawn(coordKing[0]+2,coordKing[1]+2).equals(Pawn.BLACK)) res--;
		}
		return res;
	}



	@Override
	public ITablutState getChildState(DanieleAction action) {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public int getWhitePawnsInFlowDirection() {
		// TODO Auto-generated method stub
		return whitePawnsInFlowDirection;
	}



	@Override
	public int getBlackPawnsInFlowDirection() {
		// TODO Auto-generated method stub
		return blackPawnsInFlowDirection;
	}


}

class Pos {

	int col;

	public Pos(int row, int col) {
		this.col = col;
		this.row = row;

	}
	int row;

}