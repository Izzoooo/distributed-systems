typedef i32 int
typedef list<list<int>> matrix 

exception de.hda.fbi.ds.mbredel.InvalidOperation {
	1: i32 whatOp,
	2: string why
}

service de.hda.fbi.ds.mbredel.MatrixMultiplicationService
{
    int multiply(1: matrix firstMatrix, 2: matrix secondMatrix, 3: int row, 4: int col),
    bool saveToDb()
}