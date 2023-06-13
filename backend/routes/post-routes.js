import express from 'express';
import { addPosts, getAllPosts, getById, makeLike, updatePost } from '../controllers/post-controllers';

const postRouter = express.Router();

postRouter.get("/getPost",getAllPosts);
postRouter.post("/add",addPosts);
postRouter.post("/like/:postId",makeLike);
postRouter.put("/update/:id",updatePost);
postRouter.get("/:id" , getById);


export default postRouter;

