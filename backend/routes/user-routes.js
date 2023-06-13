import express from 'express';
import { createUser, getAllUser, getById , updateLikedPost} from "../controllers/user-controllers";
import User from '../model/User';

const router = express.Router();

router.get("/",getAllUser);
router.post("/createUser",createUser);
router.get("/:id",getById);
router.post("/update-likes/:userId" , updateLikedPost);

export default router;