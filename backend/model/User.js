import mongoose from 'mongoose'

const Schema = mongoose.Schema;

const userSchema = new Schema({
	name : {
		type : String,
		required : true,
	},
	profileImage : {
		type : String,
		required : true,
	},
	posts : [{
		type : mongoose.Types.ObjectId,
		ref : "Post",
		required : true
	}],
	likedPosts: [{
		type: mongoose.Types.ObjectId,
		ref: "Post",
	}],
});

export default mongoose.model("User",userSchema);