package ibf2023.csf.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import ibf2023.csf.backend.models.Post;

@Repository
public class PictureRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO Task 4.2
	// You may change the method signature by adding parameters and/or the return type
	// You may throw any exception 

	/*
		db.travelpics.insertOne({ 
			date: ISODate("2024-06-14T05:38:29.825+0000"), 
			title: "My first post", 
			comments: "Hello there", 
			url: "https://csf-webcam-app.sgp1.digitaloceanspaces.com/6e5fb93f",
			pictureSize: 57534
		})
	 */
	// Save single post
	public String save(Post post) {
		Post savedPost = mongoTemplate.insert(post, "travelpics");
		return savedPost.get_id().toString();
	}

	/*
		db.travelpics.aggregate([
			{
				$project:
					{
						year: { $year: "$date" },
						month: { $month: "$date" },
						pictureSize: 1
					}
			},
			{
				$match: { month : 6 }
			},
			{
				$group: {
					_id: null,
					pictureSizeCount: { $sum : "$pictureSize" }
				}
			}
		])
	 */
	public Optional<Long> getTotalFileSizeInMonth(LocalDateTime date) {
		// check the month from the upload date
		// check mongo for all the records that match the month e.g. 6
		// get all the file size
		int monthVal = date.getMonthValue();

		ProjectionOperation projectOpt = Aggregation
			.project("_id", "pictureSize")
			.andExpression("{ $year: '$date' }").as("year")
			.andExpression("{ $month: '$date' }").as("month");

		MatchOperation matchOpt = Aggregation.match(Criteria.where("month").is(monthVal));

		GroupOperation groupOpt = Aggregation.group().sum("pictureSize").as("pictureSizeCount");

		Aggregation pipeline = Aggregation.newAggregation(projectOpt, matchOpt, groupOpt);
		AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "travelpics", Document.class);
		Document doc = results.getUniqueMappedResult();

		Optional<Long> totalFileSize = Optional.of(Long.parseLong(doc.get("pictureSizeCount").toString()));

		return totalFileSize;
	}
}