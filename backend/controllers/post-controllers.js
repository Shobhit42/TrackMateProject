import mongoose, { mongo } from "mongoose";
import Post from "../model/Post";
import User from "../model/User";
import { response } from "express";

export const getAllPosts = async (req, res, next) => {
    let posts;
    try {
      posts = await Post.find();
    } catch (error) {
      console.log(error);
      return res.sendStatus(500).send(error.message);
    }
  
    if (!posts || posts.length === 0) {
      return res.status(404).json({
        message: "No Posts"
      });
    }
  
    try {
      let { page, size } = req.query;
      if (!page) {
        page = 1;
      }
      if (!size) {
        size = 5;
      }
      const limit = parseInt(size);
      const skip = (parseInt(page) - 1) * limit;
      const totalCount = posts.length;
      const paginatedPosts = posts.slice(skip, skip + limit);
  
      return res.status(200).json({
        posts: paginatedPosts,
        limit,
        skip,
        totalCount
      });
    } catch (error) {
      console.log(error);
      return res.sendStatus(500).send(error.message);
    }
  }
  

export const addPosts = async(req , res , next) => {
    const { caloriesBurned , avgSpeed , duration , postImage ,likes, user , userProfile , userName , date} = req.body;

    let existingUser;
    try{
        existingUser = await User.findById(user);
    }catch(err){
        return console.log(err);
    }

    if(!existingUser){
        return res.status(400).json({
            message : "Unable to find user by this ID"
        })
    }

    const post = new Post({
        caloriesBurned,avgSpeed,duration,postImage ,likes,user,userProfile,userName , date
    });

    try {
        const session = await mongoose.startSession();
        session.startTransaction();
        await post.save({session});
        existingUser.posts.push(post);
        await existingUser.save({session})
        await session.commitTransaction();
    } catch (error) {
        return console.log(error)
        res.status(500).json({
            message : error
        })
    }

    return res.status(200).json({post});
}

export const makeLike = async(req , res , next) => {
    const { postId } = req.params;
  const { userId } = req.body;

  try {
    // Find the user by their ID
    const user = await User.findById(userId);

    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Check if the user has already liked the post
    if (user.likedPosts.includes(postId)) {
      return res.status(400).json({ message: 'Post already liked by the user' });
    }

    // Add the post ID to the user's likedPosts array
    user.likedPosts.push(postId);

    // Save the updated user
    await user.save();

    return res.status(200).json({ message: 'Post liked successfully' });
  } catch (error) {
    console.log(error);
    return res.status(500).json({ message: 'Something went wrong' });
  }
  };
  

export const updatePost = async(req , res , next) => {
    const {likes} = req.body;
    const postId = req.params.id;
    let post;
    try {
        post = await Post.findByIdAndUpdate(postId , {
            likes
        })
    } catch (error) {
        return console.log(error);
    }

    if(!post){
        return res.status(500).json({
            message : "Unable to Update"
        });
    }
    return res.status(200).json({post});
};

export const getById = async (req , res , next) => {
    const id = req.params.id;
    let post;
    try {
        post = await Post.findById(id);
    } catch (error) {
        return console.log(error);
    }

    if(!post){
        return res.status(404).json({
            message : "No Post Found"
        })
    }
    return res.status(200).json({post});
};
