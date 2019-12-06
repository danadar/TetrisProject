public class DarrenBrain implements Brain {

	@Override
	/**
	 Given a piece and a board, returns a move object that represents
	 the best play for that piece, or returns null if no play is possible.
	 See the Brain interface for details.
	*/
public int bestMove(Board board, Piece piece, int pieceX, int pieceY, int limitHeight)  {
		
		double bestScore = 1e20;
		int bestX = 0;
		int bestY = 0;
		Piece bestPiece = null;
		Piece current = piece;
		char pieceType = getPieceType(piece);
		boolean tuckMode = false;
		boolean lTuck = false;
		// loop through all the rotations
		while (true) {
			final int yBound = limitHeight - current.getHeight()+1;
			final int xBound = board.getWidth() - current.getWidth()+1;
			
			// For current rotation, try all the possible columns
			for (int x = 0; x<xBound; x++) {
				int y = board.dropHeight(current, x);
				if (y<yBound) {	// piece does not stick up too far
					int result = board.place(current, x, y);
					if (result <= Board.PLACE_ROW_FILLED) {
						if (result == Board.PLACE_ROW_FILLED) {
							board.clearRows();
						}
						double score = rateBoard(board);
						
						if (score<bestScore) {
							bestScore = score;
							bestX = x;
							bestY = y;
							bestPiece = current;
							tuckMode = false;
						}
					}
					
					board.undo();	// back out that play, loop around for the next
				}
			}
			
			//Code for Tucking
			//Looks for holes, and if the hole can be tucked into, rates the tuck as a move
			switch(pieceType)
			{
			case 't':
				for(int x = 0; x < board.getWidth() - 3; x++)
				{
					int y = board.getColumnHeight(x) - 2;	// addr of first possible hole
					
					while (y>=0) {
						//verifies if a tuck can be done to the left
						if  (!board.getGrid(x,y) && !board.getGrid(x+1, y) && !board.getGrid(x+2, y) && !board.getGrid(x+3, y) && board.dropHeight(current, x) <= y) {
							int result = board.place(current, x, y);
							
							if (result <= Board.PLACE_ROW_FILLED) {
								if (result == Board.PLACE_ROW_FILLED)
									board.clearRows();
								
								
								double score = rateBoard(board);
								
								if (score<bestScore) {
									bestScore = score;
									bestX = x;
									bestY = y;
									bestPiece = current;
									tuckMode = true;
									lTuck = true;//Left Tuck
								}
							}
						}
						
						board.undo();
						y--;
					}
				}
				for(int x = board.getWidth() - 1; x > 3; x--)
				{
					int y = board.getColumnHeight(x) - 2;
					
					while(y >= 0)
					{
						if  (!board.getGrid(x,y) && !board.getGrid(x-1, y) && !board.getGrid(x-2, y) && !board.getGrid(x-3, y)) 
						{
							int result = board.place(current, x, y);
							
							if (result <= Board.PLACE_ROW_FILLED) {
								if (result == Board.PLACE_ROW_FILLED) {
									board.clearRows();
								}
								
								double score = rateBoard(board);
								
								if (score<bestScore) {
									bestScore = score;
									bestX = x;
									bestY = y;
									bestPiece = current;
									tuckMode = true;
									lTuck = false;//Right Tuck
								}
							}
	
						}
						board.undo();
						y--;
					}
				}
				break;
			case 'i':
				for(int x = 0; x < board.getWidth() - 4; x++)
				{
					int y = board.getColumnHeight(x) - 2;	// addr of first possible hole
					
					while (y>=0) {
						//verifies if a tuck can be done to the left
						if  (!board.getGrid(x,y) && !board.getGrid(x+1, y) && !board.getGrid(x+2, y) && !board.getGrid(x+3, y) && !board.getGrid(x+4, y) && board.dropHeight(current, x) <= y) {
							int result = board.place(current, x, y);
							
							if (result <= Board.PLACE_ROW_FILLED) {
								if (result == Board.PLACE_ROW_FILLED)
									board.clearRows();
								
								
								double score = rateBoard(board);
								
								if (score<bestScore) {
									bestScore = score;
									bestX = x;
									bestY = y;
									bestPiece = current;
									tuckMode = true;
									lTuck = true;//Left Tuck
								}
							}
						}
						
						board.undo();
						y--;
					}
				}
				for(int x = board.getWidth() - 1; x > 4; x--)
				{
					int y = board.getColumnHeight(x) - 2;
					while(y >= 0)
					{
						if  (!board.getGrid(x,y) && !board.getGrid(x-1, y) && !board.getGrid(x-2, y) && !board.getGrid(x-3, y) && !board.getGrid(x-4, y) && board.dropHeight(current, x) <= y) 
						{
							int result = board.place(current, x, y);
							
							if (result <= Board.PLACE_ROW_FILLED) {
								if (result == Board.PLACE_ROW_FILLED) {
									board.clearRows();
								}
								
								double score = rateBoard(board);
								
								if (score<bestScore) {
									bestScore = score;
									bestX = x;
									bestY = y;
									bestPiece = current;
									tuckMode = true;
									lTuck = false;//Right Tuck
								}
							}
	
						}
						board.undo();
						y--;
					}
				}
				break;
			case 'l':
				break;
			}
			
			current = current.nextRotation();
			if (current == piece) break;	// break if back to original rotation
		}
		
		if (bestPiece == null)
			return(JTetris.DOWN);	// could not find a play at all!
		
		if(tuckMode)
		{
			if(!piece.equals(bestPiece))
			{
				return JTetris.ROTATE;
			}
			//Finishes tuck by moving left/right
			if(bestY == pieceY)
			{
				if(lTuck)
				{
					return JTetris.LEFT;
				}
				else
				{
					return JTetris.RIGHT;
				}
			}
			if(lTuck)
			{
				if(bestX + 1 < pieceX)
				{
					return JTetris.LEFT;
				}
				else if(bestX + 1 > pieceX)
				{
					return JTetris.RIGHT;
				}
				else
				{
					return JTetris.DOWN;
				}
			}
			else
			{
				if(bestX - 1 < pieceX)
				{
					return JTetris.LEFT;
				}
				else if(bestX - 1 > pieceX)
				{
					return JTetris.RIGHT;
				}
				else
				{
					return JTetris.DOWN;
				}
			}
		}
		
		if(!piece.equals(bestPiece))
			return JTetris.ROTATE;
		if(bestX == pieceX)
			return JTetris.DROP;
		if(bestX < pieceX)
			return JTetris.LEFT;
		else
			return JTetris.RIGHT;
		
	}
	
	
	private char getPieceType(Piece piece) {
		Piece[] Pieces = Piece.getPieces();
		
		if(piece.equals(Pieces[0]) || piece.equals(Pieces[0].nextRotation()))
		{
			return 'i';
		}
		else if(piece.equals(Pieces[1])|| piece.equals(Pieces[1].nextRotation()) || piece.equals(Pieces[1].nextRotation().nextRotation()) || piece.equals(Pieces[1].nextRotation().nextRotation().nextRotation()))
		{
			return 'l';
		}

		else if(piece.equals(Pieces[2])|| piece.equals(Pieces[2].nextRotation()) || piece.equals(Pieces[2].nextRotation().nextRotation()) || piece.equals(Pieces[2].nextRotation().nextRotation().nextRotation()))
		{
			return 'j';
		}

		else if(piece.equals(Pieces[3])|| piece.equals(Pieces[3].nextRotation()))
		{
			return 'z';
		}

		else if(piece.equals(Pieces[4])|| piece.equals(Pieces[4].nextRotation()))
		{
			return 's';
		}

		else if(piece.equals(Pieces[5]))
		{
			return 'o';
		}
		else if(piece.equals(Pieces[6])|| piece.equals(Pieces[6].nextRotation()) || piece.equals(Pieces[6].nextRotation().nextRotation()) || piece.equals(Pieces[6].nextRotation().nextRotation().nextRotation()))
		{
			return 't';
		}
		else
		{
			return 0;
		}
	}

	/*
	 A simple brain function.
	 Given a board, produce a number that rates
	 that board position -- larger numbers for worse boards.
	 This version just counts the height
	 and the number of "holes" in the board.
	 See Tetris-Architecture.html for brain ideas.
	*/
	public double rateBoard(Board board) {
		final int width = board.getWidth();
		final int maxHeight = board.getMaxHeight();
		
		int sumHeight = 0;
		int holes = 0;
		int prevColHeight = 0;
		int bigDips = 0;
		int blockiness = 0;
		int totalDipDepth = 0;
		int totalDelHeight = 0;
		
		// Count the holes, and sum up the heights
		for (int x=0; x<width; x++) {
			final int colHeight = board.getColumnHeight(x);
			sumHeight += colHeight;
			
			int y = colHeight - 2;	// addr of first possible hole
			
			while (y>=0) {
				if  (!board.getGrid(x,y)) {
					holes++;
				}
				y--;
			}
			
			totalDipDepth = maxHeight - colHeight;
			int delHeight = prevColHeight -colHeight;
			totalDelHeight += delHeight;
			
			if(x !=0 && delHeight !=0)
			{
				blockiness++;
				if(delHeight >1 || delHeight < -2)
				{
					bigDips++;
					if(x == 1 || x == width - 1)
					{
						bigDips++;
					}
				}
			}
			prevColHeight = colHeight;
		}
		
		
		double avgHeight = ((double)sumHeight)/width;
		double avgDipDepth = ((double)totalDipDepth)/width;
		double avgDelHeight = ((double)totalDelHeight)/width;
		// Add up the counts to make an overall score
		// The weights, 8, 40, etc., are just made up numbers that appear to work
		
		if(maxHeight > board.getHeight() - JTetris.TOP_SPACE)
		{
			return 1e20;
		}
		else if(avgHeight <= 5){
			return (5*maxHeight + 10*avgHeight + 50*holes + 15*bigDips + 7*avgDipDepth + -10*avgDelHeight);
		}
		else
		{	
			return (5*maxHeight + 10*avgHeight + 25*holes + 15*bigDips + 7*avgDipDepth + -10*avgDelHeight);
		}
	}
	
}