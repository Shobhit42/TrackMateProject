import { json } from "express";
import User from "../model/User"
import Post from "../model/Post";

export const getAllUser = async(req , res , next) => {
	let users;
	try {
		users = await User.find();
	} catch(e) {
		// statements
		console.log(e);
	}
	if(!users){
		return res.status(404).json({
			message : "No users found"
		});
	}
	return res.status(200).json({
		users
	});
}

export const createUser = async(req , res , next) => {
	const {name , profileImage , posts} = req.body;

	const user = new User({
		name,
		profileImage,
		posts : [],
	});


	try {
		await user.save();
		res.status(200).json({ user });
	} catch (error) {
		console.log(error);
		next(error);
	}
} 

export const updateLikedPost = async(req , res , next) => {
	try {
		const userId = req.params.userId;
		const postId = req.body.postId;
		const likes = req.body.likes;
		
		// Find the user by ID
		const user = await User.findById(userId)
		
		if (!user) {
		  return res.status(404).json({ message: 'User not found' });
		}
		
		// Check if the post ID is already present in the likedPosts array
		const likedPostIndex = user.likedPosts.findIndex((likedPost) => likedPost.equals(postId));
		
		// If the post ID is not present, add it to the likedPosts array
		if (likedPostIndex === -1) {
		  user.likedPosts.push(postId);
		} else {
		  // If the post ID is already present, remove it from the likedPosts array
		  user.likedPosts.splice(likedPostIndex, 1);
		}

		let post;
		post = await Post.findByIdAndUpdate(postId , {
            likes
        })
		
		// Save the updated user
		await user.save();
		
		return res.status(200).json({ message: 'Liked posts updated successfully' });
	  } catch (error) {
		console.error('Error updating liked posts:', error);
		return res.status(500).json({ message: 'Internal server error' });
	  }
}

export const getById = async (req , res , next) => {
    const id = req.params.id;
    let user;
    try {
        user = await User.findById(id);
    } catch (error) {
        return console.log(error);
    }

    if(!user){
        return res.status(404).json({
            message : "No User Found"
        })
    }
    return res.status(200).json({user});
};