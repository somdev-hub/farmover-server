package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.ArticleViews;
import com.farmover.server.farmover.entities.CommentArticle;
import com.farmover.server.farmover.entities.CommentVideo;
import com.farmover.server.farmover.entities.DownVoteArticle;
import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.Role;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.UpVoteArticle;
import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.entities.VideoViews;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;

@Service
public class ChartServicesImpl {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductionRepo productionRepo;

    @Autowired
    private WareHouseRepo warehouseRepo;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private ArticleRepo articleRepo;

    private final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };

    public Map<String, Double> getExpenses(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Transactions> userTransactions = user.getTransactions();

        Map<String, Double> monthWiseExpenses = new HashMap<>();

        userTransactions.forEach(transaction -> {

            if (transaction.getTransactionType().equals(TransactionType.DEBIT)) {
                Date date = transaction.getDate();
                int month = date.toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];
                Double amount = transaction.getAmount();

                monthWiseExpenses.merge(monthName, amount, Double::sum);
            }
        });

        return monthWiseExpenses;
    }

    public Map<String, Double> getRevenue(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Transactions> userTransactions = user.getTransactions();

        Map<String, Double> monthWiseIncome = new HashMap<>();

        userTransactions.forEach(transaction -> {

            if (transaction.getTransactionType().equals(TransactionType.CREDIT)) {
                Date date = transaction.getDate();
                int month = date.toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];
                Double amount = transaction.getAmount();

                monthWiseIncome.merge(monthName, amount, Double::sum);
            }
        });

        return monthWiseIncome;
    }

    public Map<String, Long> getMonthlyProductionTally(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Production> userProductions = productionRepo.findByFarmer(user)
                .orElseThrow(() -> new ResourceNotFoundException("Production", "farmer", user.getEmail()));

        Map<String, Long> monthlyProductionTally = new HashMap<>();

        userProductions.forEach(production -> {
            // Assuming Production has a getDate() method returning a Date object
            int month = production.getDate().getMonthValue();
            String monthName = MONTHS[month - 1];
            Long quantity = production.getQuantity();

            monthlyProductionTally.merge(monthName, quantity, Long::sum);
        });

        return monthlyProductionTally;
    }

    public Map<String, Double> getWarehouseUsageChart(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> usageMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(booking -> {
                if (booking.getBookingDate().getMonth().equals(currentMonth.getMonth())) {
                    usageMap.merge(storage.getStorageType().toString(), booking.getBookedWeight(), Double::sum);
                }
            });
        });

        return usageMap;
    }

    public Map<String, Double> getWarehouseRevenueFromBookings(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> revenueMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(booking -> {
                if (booking.getBookingDate().getMonth().equals(currentMonth.getMonth())) {
                    revenueMap.merge(storage.getStorageType().toString(), booking.getBookedPrice(),
                            Double::sum);
                }
            });
        });

        return revenueMap;
    }

    public Map<String, Double> getWarehouseRevenueFromSales(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> revenueMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getWarehouseSales().forEach(sale -> {
            if (sale.getDate().toLocalDate().getMonth().equals(currentMonth.getMonth())) {
                revenueMap.merge(sale.getStorageType().toString(), sale.getPrice(), Double::sum);
            }
        });

        return revenueMap;
    }

    public Map<String, Map<String, Integer>> getViewCountByRoles(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Map<String, Map<String, Integer>> viewCountByRoles = new HashMap<>();

        ArrayList<VideoDetail> videos = videoRepo.findByAuthor(user);

        ArrayList<ArticleDetail> articles = articleRepo.findByAuthor(user);

        videos.stream().forEach(video -> {
            List<VideoViews> views = video.getViews();
            views.stream().forEach(view -> {
                User viewer = userRepo.findByEmail(view.getViewerEmail()).orElse(null);
                if (viewer != null) {
                    Role role = viewer.getRole();
                    // System.out.println("Role: " + role);

                    if (viewCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> viewCount = viewCountByRoles.get(role.toString());
                        viewCount.merge("videos", 1, Integer::sum);
                    } else {
                        Map<String, Integer> viewCount = new HashMap<>();
                        viewCount.put("videos", 1);
                        viewCountByRoles.put(role.toString(), viewCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", view.getViewerEmail());
                }
            });
        });

        articles.stream().forEach(article -> {
            List<ArticleViews> views = article.getArticleViews();
            views.stream().forEach(view -> {
                User viewer = userRepo.findByEmail(view.getEmail()).orElse(null);
                if (viewer != null) {
                    Role role = viewer.getRole();

                    if (viewCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> viewCount = viewCountByRoles.get(role.toString());
                        viewCount.merge("articles", 1, Integer::sum);
                    } else {
                        Map<String, Integer> viewCount = new HashMap<>();
                        viewCount.put("articles", 1);
                        viewCountByRoles.put(role.toString(), viewCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", view.getEmail());
                }
            });
        });

        return viewCountByRoles;
    }

    public Map<String, Map<String, Integer>> getViewsCountByMonths(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Map<String, Map<String, Integer>> viewsCountByMonths = new HashMap<>();

        ArrayList<VideoDetail> videos = videoRepo.findByAuthor(user);

        ArrayList<ArticleDetail> articles = articleRepo.findByAuthor(user);

        videos.stream().forEach(video -> {
            List<VideoViews> views = video.getViews();
            views.stream().forEach(view -> {
                int month = view.getDate().toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];

                if (viewsCountByMonths.containsKey(monthName)) {
                    Map<String, Integer> viewCount = viewsCountByMonths.get(monthName);
                    viewCount.merge("videos", 1, Integer::sum);
                } else {
                    Map<String, Integer> viewCount = new HashMap<>();
                    viewCount.put("videos", 1);
                    viewsCountByMonths.put(monthName, viewCount);
                }
            });
        });

        articles.stream().forEach(article -> {
            List<ArticleViews> views = article.getArticleViews();
            views.stream().forEach(view -> {
                int month = view.getDate().toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];

                if (viewsCountByMonths.containsKey(monthName)) {
                    Map<String, Integer> viewCount = viewsCountByMonths.get(monthName);
                    viewCount.merge("articles", 1, Integer::sum);
                } else {
                    Map<String, Integer> viewCount = new HashMap<>();
                    viewCount.put("articles", 1);
                    viewsCountByMonths.put(monthName, viewCount);
                }
            });
        });

        return viewsCountByMonths;
    }

    public Map<String, Map<String, Integer>> getEngagementsCountByRoles(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Map<String, Map<String, Integer>> engagementsCountByRoles = new HashMap<>();

        ArrayList<VideoDetail> videos = videoRepo.findByAuthor(user);

        ArrayList<ArticleDetail> articles = articleRepo.findByAuthor(user);

        videos.stream().forEach(video -> {
            List<CommentVideo> comments = video.getVideoComment();
            List<UpVoteVideo> upVotes = video.getUpVoteVideo();
            List<DownVoteVideo> downVotes = video.getDownVoteVideo();

            comments.stream().forEach(comment -> {
                User commenter = userRepo.findByEmail(comment.getEmail()).orElse(null);
                if (commenter != null) {
                    Role role = commenter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("videos", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("videos", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", comment.getEmail());
                }
            });

            upVotes.stream().forEach(upVote -> {
                User voter = userRepo.findByEmail(upVote.getEmail()).orElse(null);
                if (voter != null) {
                    Role role = voter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("videos", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("videos", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", upVote.getEmail());
                }
            });

            downVotes.stream().forEach(downVote -> {
                User voter = userRepo.findByEmail(downVote.getEmail()).orElse(null);
                if (voter != null) {
                    Role role = voter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("videos", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("videos", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", downVote.getEmail());
                }
            });
        });

        articles.stream().forEach(article -> {
            List<CommentArticle> comments = article.getArticleComment();
            List<UpVoteArticle> upVotes = article.getUpVoteArticle();
            List<DownVoteArticle> downVotes = article.getDownVoteArticle();

            comments.stream().forEach(comment -> {
                User commenter = userRepo.findByEmail(comment.getEmail()).orElse(null);
                if (commenter != null) {
                    Role role = commenter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("articles", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("articles", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", comment.getEmail());
                }
            });

            upVotes.stream().forEach(upVote -> {
                User voter = userRepo.findByEmail(upVote.getEmail()).orElse(null);
                if (voter != null) {
                    Role role = voter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("articles", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("articles", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", upVote.getEmail());
                }
            });

            downVotes.stream().forEach(downVote -> {
                User voter = userRepo.findByEmail(downVote.getEmail()).orElse(null);
                if (voter != null) {
                    Role role = voter.getRole();
                    if (engagementsCountByRoles.containsKey(role.toString())) {
                        Map<String, Integer> engagementCount = engagementsCountByRoles.get(role.toString());
                        engagementCount.merge("articles", 1, Integer::sum);
                    } else {
                        Map<String, Integer> engagementCount = new HashMap<>();
                        engagementCount.put("articles", 1);
                        engagementsCountByRoles.put(role.toString(), engagementCount);
                    }
                } else {
                    throw new ResourceNotFoundException("User", "email", downVote.getEmail());
                }
            });
        });

        return engagementsCountByRoles;
    }

    // public Map<String>
}
