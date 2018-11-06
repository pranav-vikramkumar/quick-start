/*
A simple In memory repository.

In production, Replace this with a connection to a standard DB like mongoDB or MySQL
Use the connection string to establish a connection and send DB queries to these databases.
*/
let authorsDb = [
	{
			id:101,
			name:"Pranav",
			age:26
	},
	{
		id:102,
		name:"Dan Brown",
		age:50
	}
]
let booksDb = [
	{
		id:1,
		name:"Core Java",
		genre:"Programming",
		authorId:101		
	},
	{
		id:2,
		name:"Angels and Demons",
		genre:"Fiction",
		authorId:102
	}
]
let getAuthors = ()=>{
	//Get all authors
	return authorsDb;
}
let getAuthorById = (id)=>{
	//Get only one author by id
	return authorsDb.find(author => author.id == id);
}
let getBooks = ()=>{
	//Get all books
	return booksDb;
}
let getBookById = (id)=>{
	//Get only 1 book by id
	return booksDb.find(book => book.id == id);
}
let getBooksOfAuthor = (authorId)=>{
	return booksDb.filter(book => book.authorId == authorId);
}
let addAuthor = (name,age)=>{
	//Generate a new Id
	
	//First get the highest id and add 1
	let ids = authorsDb.map(author => author.id);
	let highestId = Math.max(...ids);
	let newId = highestId + 1;
	
	//Next setup a new object
	let newAuthor = {
		id: newId,
		name: name,
		age: age
	}
	
	//Save the object
	authorsDb.push(newAuthor)
	
	//Return the Id
	return newId;
}

//The API that will be used by the GraphQL server
let Repository = {
	getAuthors,
	getAuthorById,
	getBooks,
	getBookById,
	getBooksOfAuthor,
	addAuthor
}
module.exports = Repository;