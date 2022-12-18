#pragma once

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui/highgui.hpp>

inline void findDocumentCorners(const cv::Mat& image, std::vector<cv::Point>& corners);