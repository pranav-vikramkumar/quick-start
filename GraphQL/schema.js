//Imports
const gql_tools = require('graphql-tools')
const Repository = require('./repo.js')

//Define the data types in GraphQL's Schema Definition language (SDL)
//First: Your custom data types
//Queries or SELECT statements
//Mutations or DML statements
// List is []
//Forcing all arguments as mandatory is done using !

const typeDefs = `
		
	type Author{
		id: Int,
		name: String,
		age: Int,
		books: [Book]
	}
	type Book{
		id: Int,
		name: String,
		genre: String,
		author: Author
	}
		
	type Query{
		books:[Book]
		book(id:Int):Book
		authors:[Author]	
		author(id:Int):Author
	}	

	type Mutation{
		addAuthor(name:String!, age:Int!):Author
	}
`

const resolvers = {
	//First write resolver functions for all the queries you defined above in typeDefs section
	Query: {
		books: (root, args, context, info) => {
			//Get all books
			return Repository.getBooks();
		},
		book: (root, args, context, info) => {
			//Get only 1 book by id
			return Repository.getBookById(args.id)			
		},
		authors: (root, args, context, info) => {
			//Get all authors
			return Repository.getAuthors();
		},
		author: (root, args, context, info) => {
			//Get only one author by id
			return Repository.getAuthorById(args.id)
		},
	},
	
	//Similarly, write the content of all mutations you defined in the typeDefs section
	Mutation: {
		addAuthor: (root, args, context, info) => {
			//Lets assume our classic DB gives only the Id after inserting a new object
			let newId = Repository.addAuthor(args.name,args.age);
			
			//It is good practice to return the created/edited object after mutation
			//This will prevent an additional future call
			return Repository.getAuthorById(newId);
		},
	},
	
	//Important: Nesting
	
	//One author can write many books. So we must write a nesting function
	//So we must define how, books:[Book] inside Author object (we defined in the typeDefs section),
	//can be resolved
	Author: {
		books: author => {
			return Repository.getBooksOfAuthor(author.id)		
		}
	},
	
	//One book is written by one author.
	//So we must define how, the author:Author inside Book object (we defined in the typeDefs section),
	//must be resolved
	Book: {
		author: book => {
			return Repository.getAuthorById(book.authorId)			
		}
	},
}

//Some tutorials use buildSchema. However it does not support mutations, nesting etc in SDL
//makeExecutableSchema supports everything in SDL
const schema = gql_tools.makeExecutableSchema({
	typeDefs,
	resolvers
});

module.exports = schema;